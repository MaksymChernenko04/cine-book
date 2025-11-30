package com.maksymchernenko.bookingservice.service;

import com.maksymchernenko.bookingservice.client.PaymentClient;
import com.maksymchernenko.bookingservice.client.ScreeningClient;
import com.maksymchernenko.bookingservice.client.UserClient;
import com.maksymchernenko.bookingservice.client.dto.payment.PaymentResponse;
import com.maksymchernenko.bookingservice.client.dto.screening.PriceDTO;
import com.maksymchernenko.bookingservice.client.dto.screening.ScreeningDTO;
import com.maksymchernenko.bookingservice.client.dto.screening.ScreeningSeatDTO;
import com.maksymchernenko.bookingservice.client.dto.user.UserDTO;
import com.maksymchernenko.bookingservice.exception.NotFoundException;
import com.maksymchernenko.bookingservice.model.*;
import com.maksymchernenko.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserClient userClient;
    private final ScreeningClient screeningClient;
    private final PaymentClient paymentClient;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserClient userClient,
                              ScreeningClient screeningClient,
                              PaymentClient paymentClient) {
        this.bookingRepository = bookingRepository;

        this.userClient = userClient;
        this.screeningClient = screeningClient;
        this.paymentClient = paymentClient;
    }

    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id = %d not found", id)));
    }

    @Override
    public Page<Booking> getBookingsByUserId(Integer userId, Pageable pageable) {
        return bookingRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Booking createBooking(Integer userId, Integer screeningId, List<Integer> seatIds) {
        UserDTO user = userClient.getUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("User with id = %d not found", userId));
        }

        ScreeningDTO screening = screeningClient.getScreeningById(screeningId);
        if (screening == null) {
            throw new NotFoundException(String.format("Screening with id = %d not found", screeningId));
        }

        String lockToken = screeningClient.lockSeats(screeningId, seatIds).getLockToken();
        if (lockToken == null) {
            throw new IllegalStateException("Lock token was not returned from screening-service");
        }

        List<ScreeningSeatDTO> seats = screeningClient.getSeatsByScreeningAndLockToken(screeningId, lockToken);
        if (seats == null || seats.isEmpty()) {
            throw new IllegalStateException("No seats returned after locking");
        }

        BigDecimal total = seats.stream()
                .map(s -> {
                    PriceDTO p = s.getPrice();
                    return (p != null && p.getAmount() != null) ? p.getAmount() : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Price totalPrice = Price.builder()
                .amount(total)
                .currency(Price.Currency.valueOf(seats.getFirst().getPrice().getCurrency()))
                .build();

        Booking booking = Booking.builder()
                .userId(user.getId())
                .screeningId(screening.getId())
                .status(Booking.Status.PENDING)
                .totalPrice(totalPrice)
                .seatIds(seats.stream()
                        .map(ScreeningSeatDTO::getId)
                        .collect(Collectors.toList()))
                .lockToken(lockToken)
                .build();

        booking = bookingRepository.save(booking);

        PaymentResponse paymentResponse = paymentClient.initPayment(booking.getId());
        booking.setPaymentId(paymentResponse.getId());

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public void checkoutBooking(Integer bookingId, PaymentMethod paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Booking with id = %d not found", bookingId)
                ));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be checked out");
        }

        if (booking.getPaymentId() == null) {
            throw new IllegalStateException("No payment initialized for booking " + bookingId);
        }

        String lockToken = booking.getLockToken();
        if (lockToken == null) {
            throw new IllegalStateException("No lock token found for booking " + bookingId);
        }

        try {
            paymentClient.confirmPayment(booking.getPaymentId(), paymentMethod);
            screeningClient.confirmSeats(booking.getScreeningId(), lockToken);

            booking.setStatus(Booking.Status.CONFIRMED);
            bookingRepository.save(booking);
        } catch (Exception ex) {
            paymentClient.failPayment(booking.getPaymentId());
            screeningClient.unlockSeats(booking.getScreeningId(), lockToken);
            booking.setStatus(Booking.Status.CANCELLED);
            bookingRepository.save(booking);

            throw new IllegalStateException(
                    String.format("Payment with id = %d failed: %s",
                            booking.getPaymentId(), ex.getMessage()), ex);
        }
    }

    @Transactional
    @Override
    public void cancelBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Booking with id = %d not found", id)
                ));

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            return;
        }

        if (booking.getStatus() == Booking.Status.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel a confirmed booking without refund flow");
        }

        String lockToken = booking.getLockToken();
        if (lockToken != null) {
            screeningClient.unlockSeats(booking.getScreeningId(), lockToken);
        }

        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);
    }
}

package com.maksymchernenko.cinebook.service.impl;

import com.maksymchernenko.cinebook.exception.NotFoundException;
import com.maksymchernenko.cinebook.model.*;
import com.maksymchernenko.cinebook.repository.BookingRepository;
import com.maksymchernenko.cinebook.repository.UserRepository;
import com.maksymchernenko.cinebook.service.BookingService;
import com.maksymchernenko.cinebook.service.PaymentService;
import com.maksymchernenko.cinebook.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private final ScreeningService screeningService;
    private final PaymentService paymentService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ScreeningService screeningService,
                              PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;

        this.screeningService = screeningService;
        this.paymentService = paymentService;
    }

    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id = %d not found", id)));
    }

    @Override
    public Page<Booking> getBookingsByUserId(Integer userId, Pageable pageable) {
        return bookingRepository.findAllByUser_Id(userId, pageable);
    }

    @Override
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Booking createBooking(Integer userId, Integer screeningId, List<Integer> seatIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d not found", userId)));
        Screening screening = screeningService.getScreeningById(screeningId);

        String lockToken = screeningService.lockSeats(screeningId, seatIds);
        List<ScreeningSeat> seats = screeningService.getSeatsByScreeningAndLockToken(screeningId, lockToken);
        if (seats.isEmpty()) throw new IllegalStateException("No seats returned after locking");

        BigDecimal total = seats.stream()
                .map(s -> {
                    Price p = s.getPrice();

                    return (p != null && p.getAmount() != null) ? p.getAmount() : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Price totalPrice = Price.builder()
                .amount(total)
                .currency(seats.getFirst().getPrice().getCurrency())
                .build();

        Booking booking = Booking.builder()
                .user(user)
                .screening(screening)
                .status(Booking.Status.PENDING)
                .totalPrice(totalPrice)
                .bookedSeats(seats)
                .build();

        booking = bookingRepository.save(booking);

        Payment payment = paymentService.initPayment(booking.getId());
        booking.setPayment(payment);

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public void checkoutBooking(Integer bookingId, Payment.Method paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id = %d not found", bookingId)));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be checked out");
        }

        List<ScreeningSeat> seats = booking.getBookedSeats();

        String lockToken = seats.getFirst().getLockToken();
        if (lockToken == null) {
            throw new IllegalStateException("No lock token found for booked seats");
        }

        Payment payment = booking.getPayment();
        if (payment == null) {
            payment = Payment.builder().build();
            booking.setPayment(payment);
        }
        payment.setMethod(paymentMethod);
        bookingRepository.save(booking);

        try {
            paymentService.confirmPayment(payment.getId(), paymentMethod);
            screeningService.confirmSeats(booking.getScreening().getId(), lockToken);
            booking.setStatus(Booking.Status.CONFIRMED);

            bookingRepository.save(booking);
        } catch (Exception ex) {
            paymentService.failPayment(payment.getId());
            screeningService.unlockSeats(booking.getScreening().getId(), lockToken);
            booking.setStatus(Booking.Status.CANCELLED);

            bookingRepository.save(booking);

            throw new IllegalStateException(String.format("Payment цшер id = %d failed: ", payment.getId()) + ex.getMessage(), ex);
        }
    }

    @Transactional
    @Override
    public void cancelBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id = %d not found", id)));

        if (booking.getStatus() == Booking.Status.CANCELLED) return;

        if (booking.getStatus() == Booking.Status.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel a confirmed booking without refund flow");
        }

        List<ScreeningSeat> seats = booking.getBookedSeats();
        if (seats != null && !seats.isEmpty()) {
            String lockToken = seats.getFirst().getLockToken();
            if (lockToken != null) {
                screeningService.unlockSeats(booking.getScreening().getId(), lockToken);
            }
        }

        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);
    }
}

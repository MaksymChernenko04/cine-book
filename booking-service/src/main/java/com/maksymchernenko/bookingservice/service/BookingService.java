package com.maksymchernenko.bookingservice.service;

import com.maksymchernenko.bookingservice.model.Booking;
import com.maksymchernenko.bookingservice.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingService {

    Booking getBookingById(Integer id);

    Page<Booking> getBookingsByUserId(Integer userId, Pageable pageable);

    Page<Booking> getAllBookings(Pageable pageable);

    @Transactional
    Booking createBooking(Integer userId, Integer screeningId, List<Integer> seatIds);

    @Transactional
    void checkoutBooking(Integer bookingId, PaymentMethod paymentMethod);

    @Transactional
    void cancelBookingById(Integer id);
}

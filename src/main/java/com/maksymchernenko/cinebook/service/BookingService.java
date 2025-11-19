package com.maksymchernenko.cinebook.service;

import com.maksymchernenko.cinebook.model.Booking;
import com.maksymchernenko.cinebook.model.Payment;
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
    void checkoutBooking(Integer bookingId, Payment.Method paymentMethod);

    @Transactional
    void cancelBookingById(Integer id);
}

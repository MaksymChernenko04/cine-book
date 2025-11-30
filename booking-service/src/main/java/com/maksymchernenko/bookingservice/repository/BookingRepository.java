package com.maksymchernenko.bookingservice.repository;

import com.maksymchernenko.bookingservice.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findAllByUserId(Integer userId, Pageable pageable);
}

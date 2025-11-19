package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findAllByUser_Id(Integer userId, Pageable pageable);
}

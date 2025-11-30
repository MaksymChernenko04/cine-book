package com.maksymchernenko.screeningservice.repository;

import com.maksymchernenko.screeningservice.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
}

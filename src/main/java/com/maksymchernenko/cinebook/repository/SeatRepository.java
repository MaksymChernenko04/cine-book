package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
}

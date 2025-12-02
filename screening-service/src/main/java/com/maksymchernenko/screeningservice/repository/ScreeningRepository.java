package com.maksymchernenko.screeningservice.repository;

import com.maksymchernenko.screeningservice.model.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Page<Screening> findScreeningsByMovieIdAndStartTimeBetween(Integer movieId, LocalDateTime startTimeAfter, LocalDateTime startTimeBefore, Pageable pageable);
}

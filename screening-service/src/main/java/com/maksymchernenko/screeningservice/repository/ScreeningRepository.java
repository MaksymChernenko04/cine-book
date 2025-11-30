package com.maksymchernenko.screeningservice.repository;

import com.maksymchernenko.screeningservice.model.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Page<Screening> findScreeningsByMovieIdAndStartTime_Date(Integer movieId, LocalDate startTimeDate, Pageable pageable);
}

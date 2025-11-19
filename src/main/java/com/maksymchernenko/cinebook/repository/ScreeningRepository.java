package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.Movie;
import com.maksymchernenko.cinebook.model.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Page<Screening> findScreeningsByMovie_AndStartTime_Date(Movie movie, LocalDate startTimeDate, Pageable pageable);
}

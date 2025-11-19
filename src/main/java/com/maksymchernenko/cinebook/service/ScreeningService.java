package com.maksymchernenko.cinebook.service;

import com.maksymchernenko.cinebook.model.Hall;
import com.maksymchernenko.cinebook.model.Screening;
import com.maksymchernenko.cinebook.model.ScreeningSeat;
import com.maksymchernenko.cinebook.model.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {

    Integer LOCKING_TIME_IN_SECONDS = 15 * 60;

    Page<Screening> getScreeningsByMovieAndDate(Integer movieId, LocalDate date, Pageable pageable);

    Screening getScreeningById(Integer id);

    Page<Screening> getAllScreenings(Pageable pageable);

    Screening createScreening(Screening screening);

    Screening updateScreening(Screening screening);

    void deleteScreeningById(Integer screening);

    Hall getHallByScreeningId(Integer screeningId);

    Hall getHallById(Integer id);

    Page<Hall> getAllHalls(Pageable pageable);

    Hall createHall(Hall hall);

    Hall updateHall(Hall hall);

    void deleteHallById(Integer id);

    Hall addSeatByHallId(Integer hallId, Seat seat);

    Hall removeSeatByHallId(Integer hallId, Seat seat);

    List<Seat> getSeatsByHallId(Integer hallId);

    List<ScreeningSeat> getSeatsByScreeningAndLockToken(Integer screeningId, String lockToken);

    Seat createSeat(Seat seat);

    Seat updateSeat(Seat seat);

    void deleteSeatById(Integer id);

    String lockSeats(Integer screeningId, List<Integer> seatIds);

    void confirmSeats(Integer screeningId, String lockToken);

    void unlockSeats(Integer screeningId, String lockToken);
}

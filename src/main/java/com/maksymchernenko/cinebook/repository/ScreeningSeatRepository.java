package com.maksymchernenko.cinebook.repository;

import com.maksymchernenko.cinebook.model.ScreeningSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreeningSeatRepository extends JpaRepository<ScreeningSeat, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s from ScreeningSeat s where s.screening.id = :screeningId and s.seat.id in :seatIds")
    List<ScreeningSeat> findByScreeningIdAndSeatIdInForUpdate(@Param("screeningId") Integer screeningId,
                                                              @Param("seatIds") List<Integer> seatIds);

    List<ScreeningSeat> findAllByScreening_IdAndLockToken(Integer screeningId, String lockToken);

    void deleteByScreeningId(Integer screeningId);
}

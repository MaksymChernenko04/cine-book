package com.maksymchernenko.bookingservice.client;

import com.maksymchernenko.bookingservice.client.dto.screening.LockSeatsResponse;
import com.maksymchernenko.bookingservice.client.dto.screening.ScreeningDTO;
import com.maksymchernenko.bookingservice.client.dto.screening.ScreeningSeatDTO;

import java.util.List;

public interface ScreeningClient {

    ScreeningDTO getScreeningById(Integer screeningId);

    LockSeatsResponse lockSeats(Integer screeningId, List<Integer> seatIds);

    List<ScreeningSeatDTO> getSeatsByScreeningAndLockToken(Integer screeningId, String lockToken);

    void confirmSeats(Integer screeningId, String lockToken);

    void unlockSeats(Integer screeningId, String lockToken);
}
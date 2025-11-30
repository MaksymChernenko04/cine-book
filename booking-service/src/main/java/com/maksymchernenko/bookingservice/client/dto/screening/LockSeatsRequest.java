package com.maksymchernenko.bookingservice.client.dto.screening;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatsRequest {

    @NotNull(message = "Seat ids must be not null")
    private List<Integer> seatIds;
}
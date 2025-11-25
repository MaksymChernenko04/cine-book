package com.maksymchernenko.cinebook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Booking user id must be not null")
    private Integer userId;

    @NotNull(message = "Booking screening id must be not null")
    private Integer screeningId;

    @NotEmpty(message = "Booking bookedSeats must contain at least one seat")
    private List<Integer> seatIds;
}

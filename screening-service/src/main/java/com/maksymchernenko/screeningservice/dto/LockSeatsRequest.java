package com.maksymchernenko.screeningservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatsRequest {

    @NotEmpty(message = "Seat ids list must not be empty")
    private List<@NotNull(message = "Seat id must not be null") Integer> seatIds;
}
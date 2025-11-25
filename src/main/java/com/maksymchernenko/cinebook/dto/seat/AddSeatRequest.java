package com.maksymchernenko.cinebook.dto.seat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSeatRequest {

    @NotNull(message = "seatId is required")
    private Integer seatId;
}

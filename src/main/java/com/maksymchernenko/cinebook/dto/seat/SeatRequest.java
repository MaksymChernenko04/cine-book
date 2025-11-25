package com.maksymchernenko.cinebook.dto.seat;

import com.maksymchernenko.cinebook.model.Seat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatRequest {

    @NotNull(message = "Hall id must be not null")
    private Integer hallId;

    @NotNull(message = "Seat row must be not null")
    @Min(value = 1, message = "Seat row must be a positive number")
    private Integer row;

    @NotNull(message = "Seat number must be not null")
    @Min(value = 1, message = "Seat number must be a positive number")
    private Integer number;

    @NotNull(message = "Seat type must be not null")
    private Seat.Type type;

    public static Seat toEntity(SeatRequest seatRequest) {
        return Seat.builder()
                .row(seatRequest.getRow())
                .number(seatRequest.getNumber())
                .type(seatRequest.getType())
                .build();
    }
}

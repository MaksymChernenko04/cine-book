package com.maksymchernenko.bookingservice.client.dto.screening;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreeningDTO {

    @NotNull(message = "Screening id must be not null")
    private Integer id;

    @NotNull(message = "Movie id must be not null")
    private Integer movieId;

    @NotNull(message = "Hall id must be not null")
    private Integer hallId;

    @NotNull(message = "Screening startTime must be not null")
    private LocalDateTime startTime;
}
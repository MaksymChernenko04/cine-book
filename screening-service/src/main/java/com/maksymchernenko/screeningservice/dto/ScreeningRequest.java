package com.maksymchernenko.screeningservice.dto;

import com.maksymchernenko.screeningservice.model.Hall;
import com.maksymchernenko.screeningservice.model.Screening;
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
public class ScreeningRequest {

    @NotNull(message = "Movie id must be not null")
    private Integer movieId;

    @NotNull(message = "Hall id must be not null")
    private Integer hallId;

    @NotNull(message = "Screening start time must be not null")
    private LocalDateTime startTime;

    public static Screening toEntity(ScreeningRequest screeningRequest) {
        return Screening.builder()
                .movieId(screeningRequest.getMovieId())
                .hall(Hall.builder().id(screeningRequest.getHallId()).build())
                .startTime(screeningRequest.getStartTime())
                .build();
    }
}

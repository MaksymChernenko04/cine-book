package com.maksymchernenko.cinebook.dto.screening;

import com.maksymchernenko.cinebook.model.Hall;
import com.maksymchernenko.cinebook.model.Movie;
import com.maksymchernenko.cinebook.model.Screening;
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
                .movie(Movie.builder().id(screeningRequest.getMovieId()).build())
                .hall(Hall.builder().id(screeningRequest.getHallId()).build())
                .startTime(screeningRequest.getStartTime())
                .build();
    }
}

package com.maksymchernenko.cinebook.dto;

import com.maksymchernenko.cinebook.model.Hall;
import com.maksymchernenko.cinebook.model.Movie;
import com.maksymchernenko.cinebook.model.Screening;
import jakarta.persistence.ManyToOne;
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

    @NotNull(message = "Screening movie must be not null")
    @ManyToOne(optional = false)
    private Movie movie;

    @NotNull(message = "Screening start time must be not null")
    private LocalDateTime startTime;

    @NotNull(message = "Screening hall must be not null")
    @ManyToOne(optional = false)
    private Hall hall;

    public static Screening toEntity(ScreeningRequest screeningRequest) {
        return Screening.builder()
                .movie(screeningRequest.getMovie())
                .startTime(screeningRequest.getStartTime())
                .hall(screeningRequest.getHall())
                .build();
    }
}

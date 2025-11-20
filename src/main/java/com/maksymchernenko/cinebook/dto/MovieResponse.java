package com.maksymchernenko.cinebook.dto;

import com.maksymchernenko.cinebook.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {

    private Integer id;
    private String title;
    private String description;
    private String genre;
    private Integer durationMinutes;
    private BigDecimal rating;
    private LocalDate releaseDate;
    private String posterURL;

    public static MovieResponse fromEntity(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre().toString())
                .durationMinutes(movie.getDurationMinutes())
                .rating(movie.getRating())
                .releaseDate(movie.getReleaseDate())
                .posterURL(movie.getPosterURL())
                .build();
    }
}
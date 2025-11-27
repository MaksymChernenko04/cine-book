package com.maksymchernenko.movieservice.dto;

import com.maksymchernenko.movieservice.model.Movie;
import jakarta.validation.constraints.*;
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
public class MovieRequest {

    @NotBlank(message = "Movie title cannot be blank")
    @Size(min = 1, max = 100, message = "Movie title length must be between 1 and 100")
    private String title;

    @Size(min = 1, max = 1000, message = "Movie description length must be between 1 and 1000")
    private String description;

    @NotNull(message = "Movie genre must be not null")
    private Movie.Genre genre;

    @NotNull(message = "Movie duration must be not null")
    @Min(value = 1, message = "Movie duration must be a positive number")
    @Max(value = 600, message = "Movie duration can not be longer than 600 minutes")
    private Integer durationMinutes;

    @Digits(integer = 2, fraction = 1)
    @DecimalMin(value = "0.0", inclusive = false, message = "Movie rating must be a positive number")
    @DecimalMax(value = "10.0", message = "Movie rating can not be more than 10.0")
    private BigDecimal rating;

    private LocalDate releaseDate;

    @Pattern(regexp = "https?://.+\\.(jpg|jpeg|png|JPG|JPEG|PNG)", message = "Movie poster URL must be a valid jpg, jpeg or png file")
    @Size(max = 250, message = "Movie poster URL can not be longer than 250")
    private String posterURL;

    public static Movie toEntity(MovieRequest movieRequest) {
        return Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .genre(movieRequest.getGenre())
                .durationMinutes(movieRequest.getDurationMinutes())
                .rating(movieRequest.getRating())
                .releaseDate(movieRequest.getReleaseDate())
                .posterURL(movieRequest.getPosterURL())
                .build();
    }
}

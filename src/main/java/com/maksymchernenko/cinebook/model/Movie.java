package com.maksymchernenko.cinebook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Movie title cannot be blank")
    @Size(min = 1, max = 100, message = "Movie title length must be between 1 and 100")
    private String title;

    @Size(min = 1, max = 1000, message = "Movie description length must be between 1 and 1000")
    private String description;

    @NotNull(message = "Movie genre must be not null")
    private Genre genre;

    @NotNull(message = "Movie duration must be not null")
    @DecimalMin(value = "1", message = "Movie duration must be a positive number")
    @DecimalMax(value = "600", message = "Movie duration can not be longer than 600 minutes")
    private Integer durationMinutes;

    @Digits(integer = 2, fraction = 1)
    @DecimalMin(value = "0.0", inclusive = false, message = "Movie rating must be a positive number")
    @DecimalMax(value = "10.0", message = "Movie duration can not be more than 10.0")
    private Double rating;

    private LocalDate releaseDate;

    @Pattern(regexp = "https?://.+\\.(jpg|jpeg|png|JPG|JPEG|PNG)", message = "Movie poster URL must be a valid jpg, jpeg or png file")
    @Size(max = 250, message = "Movie poster URL can not be longer than 250")
    private String posterURL;

    public enum Genre {
        SCI_FI,
        ANIMATION,
        DRAMA,
        COMEDY,
        ACTION,
        CRIME,
        HORROR
    }
}

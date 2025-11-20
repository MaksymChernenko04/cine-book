package com.maksymchernenko.cinebook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
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
    private String title;
    private String description;
    private Genre genre;
    private Integer durationMinutes;
    private BigDecimal rating;
    private LocalDate releaseDate;
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

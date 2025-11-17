package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Screening movie must be not null")
    @ManyToOne(optional = false)
    private Movie movie;

    @NotNull(message = "Screening start time must be not null")
    private LocalDateTime startTime;

    @NotNull(message = "Screening hall must be not null")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Hall hall;
}

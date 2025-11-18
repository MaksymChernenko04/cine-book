package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull(message = "Seat row must be not null")
    @Min(value = 1, message = "Seat row must be a positive number")
    @Column(name = "row_number")
    private Integer row;

    @NotNull(message = "Seat number must be not null")
    @Min(value = 1, message = "Seat number must be a positive number")
    private Integer number;

    @NotNull(message = "Seat type must be not null")
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        REGULAR,
        VIP,
        ACCESSIBLE
    }
}

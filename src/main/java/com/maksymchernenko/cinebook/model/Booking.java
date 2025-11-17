package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Booking user must be not null")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private User user;

    @NotNull(message = "Booking status must be not null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Booking screening must be not null")
    @ManyToOne(optional = false)
    private Screening screening;

    @NotEmpty(message = "Booking bookedSeats must contain at least one seat")
    @ManyToMany
    private List<Seat> bookedSeats;

    @Embedded
    @NotNull(message = "Booking totalPrice must be not null")
    private Price totalPrice;

    @Embedded
    private Payment payment;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}

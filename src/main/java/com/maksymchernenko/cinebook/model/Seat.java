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

    @NotNull(message = "Seat row must be not null")
    @Min(value = 1, message = "Seat row must be a positive number")
    private Integer row;

    @NotNull(message = "Seat number must be not null")
    @Min(value = 1, message = "Seat number must be a positive number")
    private Integer number;

    @NotNull(message = "Seat type must be not null")
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull(message = "Seat status must be not null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private Price price;

    @Size(min = 16, max = 16, message = "Seat lock token must be exactly 16 characters")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Seat lock token must be alphanumeric")
    private String lockToken;

    public enum Type {
        REGULAR,
        VIP,
        ACCESSIBLE
    }

    public enum Status {
        AVAILABLE,
        LOCKED,
        SOLD
    }
}

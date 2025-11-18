package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @NotNull(message = "Screening seat status must be not null")
    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    @Size(min = 36, max = 36, message = "Lock token must be a valid UUID string of length 36")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Lock token must be a valid UUID")
    private String lockToken;

    private Instant lockedAt;

    private Instant expiresAt;

    public enum Status {
        AVAILABLE,
        LOCKED,
        SOLD
    }
}

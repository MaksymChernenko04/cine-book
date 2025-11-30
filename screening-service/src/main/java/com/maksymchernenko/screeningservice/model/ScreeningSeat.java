package com.maksymchernenko.screeningservice.model;

import jakarta.persistence.*;
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

    @Embedded
    private Price price;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    private String lockToken;

    private Instant lockedAt;

    private Instant expiresAt;

    @Version
    private Long version;

    public enum Status {
        AVAILABLE,
        LOCKED,
        SOLD
    }
}

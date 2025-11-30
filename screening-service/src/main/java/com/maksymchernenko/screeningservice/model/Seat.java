package com.maksymchernenko.screeningservice.model;

import jakarta.persistence.*;
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

    @Column(name = "row_number")
    private Integer row;

    private Integer number;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        REGULAR,
        VIP,
        ACCESSIBLE
    }
}

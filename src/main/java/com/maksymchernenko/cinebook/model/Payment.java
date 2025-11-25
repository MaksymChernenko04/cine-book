package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Method method;

    private LocalDateTime paidAt;

    private String expectedRedirectURL;

    public enum Method {
        CARD,
        CASH,
        ONLINE
    }
}
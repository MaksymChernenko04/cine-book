package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Pattern(regexp = "https?://.+", message = "Payment expectedRedirectURL must be a valid URL")
    @Size(max = 200, message = "Payment expectedRedirectURL must be no more than 200 characters")
    private String expectedRedirectURL;

    public enum Method {
        CARD,
        CASH,
        ONLINE
    }
}
package com.maksymchernenko.bookingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Price {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public enum Currency {
        USD,
        EUR,
        UAH,
        PLN
    }
}
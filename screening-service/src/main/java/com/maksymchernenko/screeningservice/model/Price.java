package com.maksymchernenko.screeningservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
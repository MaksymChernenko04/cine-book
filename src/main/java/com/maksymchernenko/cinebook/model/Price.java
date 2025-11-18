package com.maksymchernenko.cinebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Price {

    @NotNull(message = "Price value must be not null")
    @Digits(integer = 5, fraction = 2, message = "Price value must have up to 5 integer digits and up to 2 fraction digits")
    @DecimalMin(value = "0.00", message = "Price value must be greater than or equal to 0.00")
    @DecimalMax(value = "1000.00", inclusive = false, message = "Price value must be less than 1000.00")
    private BigDecimal amount;

    @NotNull(message = "Price currency must be not null")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public enum Currency {
        USD,
        EUR,
        UAH,
        PLN
    }
}
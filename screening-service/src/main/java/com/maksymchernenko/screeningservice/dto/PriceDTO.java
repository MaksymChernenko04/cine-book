package com.maksymchernenko.screeningservice.dto;

import com.maksymchernenko.screeningservice.model.Price;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDTO {

    @Min(value = 0, message = "Price amount must be a positive decimal")
    @NotNull(message = "Price amount must be not null")
    private BigDecimal amount;

    @NotNull(message = "Price currency must be not null")
    private Price.Currency currency;

    public static PriceDTO fromEntity(Price price) {
        if (price == null) return null;
        return PriceDTO.builder()
                .amount(price.getAmount())
                .currency(price.getCurrency())
                .build();
    }
}
package com.maksymchernenko.bookingservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.bookingservice.model.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JacksonXmlRootElement(localName = "price")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceResponse {

    private BigDecimal amount;
    private Price.Currency currency;

    public static PriceResponse fromEntity(Price price) {
        if (price == null) return null;
        return PriceResponse.builder()
                .amount(price.getAmount())
                .currency(price.getCurrency())
                .build();
    }
}

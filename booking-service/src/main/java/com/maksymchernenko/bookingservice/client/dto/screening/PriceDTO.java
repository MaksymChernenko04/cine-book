package com.maksymchernenko.bookingservice.client.dto.screening;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Price currency must not be blank")
    private String currency;
}

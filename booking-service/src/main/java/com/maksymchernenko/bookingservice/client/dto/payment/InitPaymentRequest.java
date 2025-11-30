package com.maksymchernenko.bookingservice.client.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitPaymentRequest {

    @NotNull(message = "Booking id must be not null")
    private Integer bookingId;
}
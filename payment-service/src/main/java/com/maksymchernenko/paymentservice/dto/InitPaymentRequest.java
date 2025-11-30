package com.maksymchernenko.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitPaymentRequest {

    @NotNull(message = "Booking id must be not null")
    private Integer bookingId;
}
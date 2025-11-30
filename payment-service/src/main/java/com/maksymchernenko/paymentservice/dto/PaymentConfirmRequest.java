package com.maksymchernenko.paymentservice.dto;

import com.maksymchernenko.paymentservice.model.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentConfirmRequest {

    @NotNull(message = "Payment method must be not null")
    private Payment.Method method;
}
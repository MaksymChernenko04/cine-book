package com.maksymchernenko.bookingservice.client.dto.payment;

import com.maksymchernenko.bookingservice.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmRequest {

    @NotNull(message = "Payment method must be not null")
    private PaymentMethod method;
}
package com.maksymchernenko.bookingservice.dto;

import com.maksymchernenko.bookingservice.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    @NotNull(message = "Payment method must be provided")
    private PaymentMethod paymentMethod;
}
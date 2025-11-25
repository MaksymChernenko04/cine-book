package com.maksymchernenko.cinebook.dto;

import com.maksymchernenko.cinebook.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    private Payment.Method paymentMethod;
}
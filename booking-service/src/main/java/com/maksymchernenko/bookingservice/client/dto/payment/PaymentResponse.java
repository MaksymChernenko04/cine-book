package com.maksymchernenko.bookingservice.client.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private Integer id;
    private String method;
    private LocalDateTime paidAt;
    private String expectedRedirectURL;
}
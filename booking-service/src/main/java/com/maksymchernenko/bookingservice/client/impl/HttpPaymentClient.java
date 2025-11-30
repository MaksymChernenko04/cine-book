package com.maksymchernenko.bookingservice.client.impl;

import com.maksymchernenko.bookingservice.client.PaymentClient;
import com.maksymchernenko.bookingservice.client.dto.payment.InitPaymentRequest;
import com.maksymchernenko.bookingservice.client.dto.payment.PaymentConfirmRequest;
import com.maksymchernenko.bookingservice.client.dto.payment.PaymentResponse;
import com.maksymchernenko.bookingservice.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpPaymentClient implements PaymentClient {

    private final RestTemplate restTemplate;

    @Value("${services.payment.base-url}")
    private String paymentServiceBaseUrl;

    @Override
    public PaymentResponse initPayment(Integer bookingId) {
        InitPaymentRequest request = new InitPaymentRequest();
        request.setBookingId(bookingId);

        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                paymentServiceBaseUrl + "/api/payments",
                request,
                PaymentResponse.class
        );

        PaymentResponse body = response.getBody();
        if (body == null || body.getId() == null) {
            throw new IllegalStateException("Payment service returned empty response or null id for booking " + bookingId);
        }

        return body;
    }

    @Override
    public void confirmPayment(Integer paymentId, PaymentMethod method) {
        PaymentConfirmRequest request = new PaymentConfirmRequest();
        request.setMethod(method);

        restTemplate.postForEntity(
                paymentServiceBaseUrl + "/api/payments/{id}/confirm",
                request,
                Void.class,
                paymentId
        );
    }

    @Override
    public void failPayment(Integer paymentId) {
        restTemplate.postForEntity(
                paymentServiceBaseUrl + "/api/payments/{id}/fail",
                null,
                Void.class,
                paymentId
        );
    }
}
package com.maksymchernenko.bookingservice.client;

import com.maksymchernenko.bookingservice.client.dto.payment.PaymentResponse;
import com.maksymchernenko.bookingservice.model.PaymentMethod;

public interface PaymentClient {

    PaymentResponse initPayment(Integer bookingId);

    void confirmPayment(Integer paymentId, PaymentMethod method);

    void failPayment(Integer paymentId);
}
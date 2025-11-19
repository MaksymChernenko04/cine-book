package com.maksymchernenko.cinebook.service;

import com.maksymchernenko.cinebook.model.Payment;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    Payment getPaymentById(Integer id);

    @Transactional
    Payment initPayment(Integer bookingId);

    @Transactional
    void confirmPayment(Integer id, Payment.Method method);

    @Transactional
    void failPayment(Integer id);
}

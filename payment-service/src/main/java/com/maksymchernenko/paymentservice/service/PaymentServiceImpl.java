package com.maksymchernenko.paymentservice.service;

import com.maksymchernenko.paymentservice.exception.NotFoundException;
import com.maksymchernenko.paymentservice.model.Payment;
import com.maksymchernenko.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id = %d not found", id)));
    }

    @Transactional
    @Override
    public Payment initPayment(Integer bookingId) {
        Payment payment = Payment.builder()
                .method(null)
                .paidAt(null)
                .expectedRedirectURL(String.format("https://pay.example.com/session/%d/%s",
                        bookingId, UUID.randomUUID()))
                .build();

        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void confirmPayment(Integer id, Payment.Method method) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id = %d not found", id)));

        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());
        payment.setExpectedRedirectURL(null);

        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public void failPayment(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id = %d not found", id)));

        payment.setExpectedRedirectURL(null);
        payment.setPaidAt(null);
        payment.setMethod(null);

        paymentRepository.save(payment);
    }
}

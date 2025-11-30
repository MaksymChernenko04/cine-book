package com.maksymchernenko.paymentservice.repository;

import com.maksymchernenko.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}

package com.maksymchernenko.paymentservice.controller;

import com.maksymchernenko.paymentservice.dto.InitPaymentRequest;
import com.maksymchernenko.paymentservice.dto.PaymentConfirmRequest;
import com.maksymchernenko.paymentservice.dto.PaymentResponse;
import com.maksymchernenko.paymentservice.model.Payment;
import com.maksymchernenko.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/payments", produces = {"application/json", "application/xml"}
)
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);

        return PaymentResponse.fromEntity(payment);
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<PaymentResponse> initPayment(@Valid @RequestBody InitPaymentRequest request) {
        Payment payment = paymentService.initPayment(request.getBookingId());
        PaymentResponse response = PaymentResponse.fromEntity(payment);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(value = "/{id}/confirm", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> confirmPayment(@PathVariable Integer id,
                                               @Valid @RequestBody PaymentConfirmRequest request) {
        paymentService.confirmPayment(id, request.getMethod());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<Void> failPayment(@PathVariable Integer id) {
        paymentService.failPayment(id);

        return ResponseEntity.noContent().build();
    }
}

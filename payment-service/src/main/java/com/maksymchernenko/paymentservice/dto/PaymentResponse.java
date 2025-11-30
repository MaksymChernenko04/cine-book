package com.maksymchernenko.paymentservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.paymentservice.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JacksonXmlRootElement(localName = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Integer id;
    private Payment.Method method;
    private LocalDateTime paidAt;
    private String expectedRedirectURL;

    public static PaymentResponse fromEntity(Payment p) {
        if (p == null) return null;

        return PaymentResponse.builder()
                .id(p.getId())
                .method(p.getMethod())
                .paidAt(p.getPaidAt())
                .expectedRedirectURL(p.getExpectedRedirectURL())
                .build();
    }
}

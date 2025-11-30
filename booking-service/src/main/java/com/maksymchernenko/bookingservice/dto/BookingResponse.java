package com.maksymchernenko.bookingservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.bookingservice.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Integer id;
    private Integer userId;
    private Integer screeningId;
    private List<Integer> seatIds;
    private Booking.Status status;
    private PriceResponse totalPrice;
    private Integer paymentId;

    public static BookingResponse fromEntity(Booking b) {
        if (b == null) return null;

        return BookingResponse.builder()
                .id(b.getId())
                .userId(b.getUserId())
                .screeningId(b.getScreeningId())
                .seatIds(b.getSeatIds())
                .status(b.getStatus())
                .totalPrice(PriceResponse.fromEntity(b.getTotalPrice()))
                .paymentId(b.getPaymentId())
                .build();
    }
}

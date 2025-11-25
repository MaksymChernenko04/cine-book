package com.maksymchernenko.cinebook.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.cinebook.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@JacksonXmlRootElement(localName = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Integer id;
    private Integer userId;
    private Booking.Status status;
    private Integer screeningId;
    private List<ScreeningSeatResponse> bookedSeats;
    private PriceResponse totalPrice;
    private PaymentResponse payment;

    public static BookingResponse fromEntity(Booking b) {
        if (b == null) return null;

        List<ScreeningSeatResponse> seats = null;
        if (b.getBookedSeats() != null) {
            seats = b.getBookedSeats().stream()
                    .map(ScreeningSeatResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        return BookingResponse.builder()
                .id(b.getId())
                .userId(b.getUser() != null ? b.getUser().getId() : null)
                .status(b.getStatus())
                .screeningId(b.getScreening() != null ? b.getScreening().getId() : null)
                .bookedSeats(seats)
                .totalPrice(PriceResponse.fromEntity(b.getTotalPrice()))
                .payment(PaymentResponse.fromEntity(b.getPayment()))
                .build();
    }
}

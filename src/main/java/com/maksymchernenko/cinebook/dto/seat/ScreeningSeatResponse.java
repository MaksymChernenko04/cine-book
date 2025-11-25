package com.maksymchernenko.cinebook.dto.seat;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.cinebook.model.Price;
import com.maksymchernenko.cinebook.model.ScreeningSeat;
import com.maksymchernenko.cinebook.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@JacksonXmlRootElement(localName = "screeningSeat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreeningSeatResponse {

    private Integer id;
    private Integer seatId;
    private Integer row;
    private Integer number;
    private ScreeningSeat.Status status;
    private String lockToken;
    private Instant lockedAt;
    private Instant expiresAt;
    private BigDecimal price;
    private Price.Currency currency;

    public static ScreeningSeatResponse fromEntity(ScreeningSeat s) {
        if (s == null) return null;

        ScreeningSeatResponse.ScreeningSeatResponseBuilder builder = ScreeningSeatResponse.builder()
                .id(s.getId())
                .status(s.getStatus())
                .lockToken(s.getLockToken())
                .lockedAt(s.getLockedAt())
                .expiresAt(s.getExpiresAt());

        if (s.getSeat() != null) {
            Seat seat = s.getSeat();
            builder.seatId(seat.getId())
                    .row(seat.getRow())
                    .number(seat.getNumber());
        }

        if (s.getPrice() != null) {
            builder.price(s.getPrice().getAmount())
                    .currency(s.getPrice().getCurrency());
        }

        return builder.build();
    }
}
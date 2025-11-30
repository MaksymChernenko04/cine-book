package com.maksymchernenko.screeningservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.screeningservice.model.ScreeningSeat;
import com.maksymchernenko.screeningservice.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private PriceDTO price;

    public static ScreeningSeatResponse fromEntity(ScreeningSeat s) {
        ScreeningSeatResponseBuilder builder = ScreeningSeatResponse.builder()
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
            builder.price(PriceDTO.fromEntity(s.getPrice()));
        }

        return builder.build();
    }
}
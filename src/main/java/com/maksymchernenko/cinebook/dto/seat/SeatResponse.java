package com.maksymchernenko.cinebook.dto.seat;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.cinebook.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "seat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {

    private Integer id;
    private Integer hallId;
    private Integer row;
    private Integer number;
    private Seat.Type type;

    public static SeatResponse fromEntity(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .hallId(seat.getHall().getId())
                .row(seat.getRow())
                .number(seat.getNumber())
                .type(seat.getType())
                .build();
    }
}

package com.maksymchernenko.screeningservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.screeningservice.model.Screening;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JacksonXmlRootElement(localName = "screening")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreeningResponse {

    private Integer id;
    private Integer movieId;
    private LocalDateTime startTime;
    private Integer hallId;
    private String hallName;

    public static ScreeningResponse fromEntity(Screening screening) {
        return ScreeningResponse.builder()
                .id(screening.getId())
                .movieId(screening.getMovieId())
                .startTime(screening.getStartTime())
                .hallId(screening.getHall().getId())
                .hallName(screening.getHall().getName())
                .build();
    }
}

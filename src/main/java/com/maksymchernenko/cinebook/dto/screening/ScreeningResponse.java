package com.maksymchernenko.cinebook.dto.screening;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.cinebook.model.Screening;
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
    private String movieTitle;
    private LocalDateTime startTime;
    private Integer hallId;
    private String hallName;

    public static ScreeningResponse fromEntity(Screening screening) {
        return ScreeningResponse.builder()
                .id(screening.getId())
                .movieId(screening.getMovie().getId())
                .movieTitle(screening.getMovie().getTitle())
                .startTime(screening.getStartTime())
                .hallId(screening.getHall().getId())
                .hallName(screening.getHall().getName())
                .build();
    }
}

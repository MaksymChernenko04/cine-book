package com.maksymchernenko.screeningservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.maksymchernenko.screeningservice.model.Hall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "hall")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallResponse {

    private Integer id;
    private String name;
    private Integer capacity;
    private Boolean accessibility;

    public static HallResponse fromEntity(Hall hall) {
        return HallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .capacity(hall.getCapacity())
                .accessibility(hall.getAccessibility())
                .build();
    }
}

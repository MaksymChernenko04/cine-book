package com.maksymchernenko.cinebook.dto.hall;

import com.maksymchernenko.cinebook.model.Hall;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallRequest {

    @NotBlank(message = "Hall name cannot be blank")
    private String name;

    @NotNull(message = "Hall capacity cannot be null")
    @Min(value = 1, message = "Hall capacity must be a positive number")
    private Integer capacity;

    @NotNull(message = "Hall accessibility flag must be not null")
    private Boolean accessibility = false;

    public static Hall toEntity(HallRequest hallRequest) {
        return Hall.builder()
                .name(hallRequest.getName())
                .capacity(hallRequest.getCapacity())
                .accessibility(hallRequest.getAccessibility())
                .build();
    }
}

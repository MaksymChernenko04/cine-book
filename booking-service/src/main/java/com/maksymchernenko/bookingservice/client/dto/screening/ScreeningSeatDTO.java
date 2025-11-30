package com.maksymchernenko.bookingservice.client.dto.screening;

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
public class ScreeningSeatDTO {

    @NotNull(message = "Seat id must be not null")
    private Integer id;

    @NotNull(message = "Seat screeningId must be not null")
    private Integer screeningId;

    @NotBlank(message = "Seat rowLabel must not be blank")
    private String rowLabel;

    @NotNull(message = "Seat number must be not null")
    @Min(value = 1, message = "Seat number must be >= 1")
    private Integer seatNumber;

    @NotNull(message = "Seat price must be not null")
    private PriceDTO price;

    @NotNull(message = "Lock token must not be blank")
    private String lockToken;
}
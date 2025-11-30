package com.maksymchernenko.screeningservice.client;

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
public class MovieDTO {

    @NotNull(message = "Movie id must not be null")
    private Integer id;

    @NotBlank(message = "Movie title must not be blank")
    private String title;
}
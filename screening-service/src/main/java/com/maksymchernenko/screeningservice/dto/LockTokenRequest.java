package com.maksymchernenko.screeningservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockTokenRequest {

    @NotBlank(message = "Lock token must not be blank")
    private String lockToken;
}
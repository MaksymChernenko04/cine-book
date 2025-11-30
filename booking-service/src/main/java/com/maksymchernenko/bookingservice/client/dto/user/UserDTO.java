package com.maksymchernenko.bookingservice.client.dto.user;

import jakarta.validation.constraints.Email;
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
public class UserDTO {

    @NotNull(message = "User id must be not null")
    private Integer id;

    @Email(message = "User email must be a valid email")
    @NotBlank(message = "User email must not be blank")
    private String email;

    @NotBlank(message = "User fullName must not be blank")
    private String fullName;
}
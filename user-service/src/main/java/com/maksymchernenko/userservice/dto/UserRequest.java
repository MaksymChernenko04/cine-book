package com.maksymchernenko.userservice.dto;

import com.maksymchernenko.userservice.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "User name must not be blank")
    @Size(min = 1, max = 200, message = "User name must be between 1 and 200 characters")
    private String name;

    @NotBlank(message = "User email must not be blank")
    @Pattern(regexp = "[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[A-Za-z]{2,}", message = "User email must be a valid email address")
    @Size(max = 100, message = "User email must be no more than 100 characters")
    private String email;

    public static User toEntity(UserRequest r) {
        if (r == null) return null;

        return User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .build();
    }
}

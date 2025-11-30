package com.maksymchernenko.bookingservice.client.impl;

import com.maksymchernenko.bookingservice.client.UserClient;
import com.maksymchernenko.bookingservice.client.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpUserClient implements UserClient {

    private final RestTemplate restTemplate;

    @Value("${services.user.base-url}")
    private String userServiceBaseUrl;

    @Override
    public UserDTO getUserById(Integer userId) {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    userServiceBaseUrl + "/api/users/{id}",
                    UserDTO.class,
                    userId);

            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
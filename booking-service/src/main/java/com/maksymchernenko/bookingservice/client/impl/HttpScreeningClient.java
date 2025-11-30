package com.maksymchernenko.bookingservice.client.impl;

import com.maksymchernenko.bookingservice.client.ScreeningClient;
import com.maksymchernenko.bookingservice.client.dto.screening.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HttpScreeningClient implements ScreeningClient {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpScreeningClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${services.screening.base-url}")
    private String screeningServiceBaseUrl;

    @Override
    public ScreeningDTO getScreeningById(Integer screeningId) {
        ResponseEntity<ScreeningDTO> response = restTemplate.getForEntity(
                screeningServiceBaseUrl + "/api/screenings/{id}",
                ScreeningDTO.class,
                screeningId);

        return response.getBody();
    }

    @Override
    public List<ScreeningSeatDTO> getSeatsByScreeningAndLockToken(Integer screeningId, String lockToken) {
        String url = screeningServiceBaseUrl + "/api/screenings/{id}/seats?lockToken={lockToken}";

        ResponseEntity<ScreeningSeatDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ScreeningSeatDTO[].class,
                screeningId,
                lockToken
        );

        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }

    @Override
    public LockSeatsResponse lockSeats(Integer screeningId, List<Integer> seatIds) {
        LockSeatsRequest request = new LockSeatsRequest();
        request.setSeatIds(seatIds);

        ResponseEntity<LockSeatsResponse> response = restTemplate.postForEntity(
                screeningServiceBaseUrl + "/api/screenings/{id}/seats/lock",
                request,
                LockSeatsResponse.class,
                screeningId);

        return response.getBody();
    }

    @Override
    public void confirmSeats(Integer screeningId, String lockToken) {
        LockTokenRequest request = new LockTokenRequest();
        request.setLockToken(lockToken);

        restTemplate.postForEntity(
                screeningServiceBaseUrl + "/api/screenings/{id}/seats/confirm",
                request,
                Void.class,
                screeningId);
    }

    @Override
    public void unlockSeats(Integer screeningId, String lockToken) {
        LockTokenRequest request = new LockTokenRequest();
        request.setLockToken(lockToken);

        restTemplate.postForEntity(
                screeningServiceBaseUrl + "/api/screenings/{id}/seats/unlock",
                request,
                Void.class,
                screeningId);
    }
}
package com.maksymchernenko.screeningservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpMovieClient implements MovieClient {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpMovieClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${services.movie.base-url}")
    private String movieServiceBaseUrl;

    @Override
    public MovieDTO getMovieById(Integer id) {
        ResponseEntity<MovieDTO> response = restTemplate.getForEntity(
                movieServiceBaseUrl + "/api/movies/{id}",
                MovieDTO.class,
                id);

        return response.getBody();
    }
}

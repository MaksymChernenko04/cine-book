package com.maksymchernenko.cinebook.controller;

import com.maksymchernenko.cinebook.dto.SeatRequest;
import com.maksymchernenko.cinebook.dto.SeatResponse;
import com.maksymchernenko.cinebook.model.Hall;
import com.maksymchernenko.cinebook.model.Seat;
import com.maksymchernenko.cinebook.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/seats", produces = {"application/json", "application/xml"})
public class SeatController {

    private final ScreeningService screeningService;

    @Autowired
    public SeatController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/by-hall/{hallId}")
    public List<SeatResponse> getSeatsByHallId(@PathVariable Integer hallId) {
        return screeningService.getSeatsByHallId(hallId).stream()
                .map(SeatResponse::fromEntity)
                .toList();
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<SeatResponse> createSeat(@Valid @RequestBody SeatRequest seatRequest) {
        Seat seat = SeatRequest.toEntity(seatRequest);
        seat.setHall(com.maksymchernenko.cinebook.model.Hall.builder()
                .id(seatRequest.getHallId())
                .build());

        Seat savedSeat = screeningService.createSeat(seat);
        SeatResponse response = SeatResponse.fromEntity(savedSeat);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable Integer id,
                                                   @Valid @RequestBody SeatRequest seatRequest) {
        Seat seat = SeatRequest.toEntity(seatRequest);
        seat.setId(id);

        if (seatRequest.getHallId() != null) {
            seat.setHall(Hall.builder().id(seatRequest.getHallId()).build());
        }

        Seat saved = screeningService.updateSeat(seat);

        return ResponseEntity.ok(SeatResponse.fromEntity(saved));
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> deleteSeatById(@PathVariable Integer id) {
        screeningService.deleteSeatById(id);

        return ResponseEntity.noContent().build();
    }


}

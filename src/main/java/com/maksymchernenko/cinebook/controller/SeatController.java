package com.maksymchernenko.cinebook.controller;

import com.maksymchernenko.cinebook.dto.SeatRequest;
import com.maksymchernenko.cinebook.dto.SeatResponse;
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
        SeatResponse saved = SeatResponse.fromEntity(screeningService.createSeat(seat));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable Integer id,
                                                   @Valid @RequestBody SeatRequest seatRequest) {
        Seat seat = SeatRequest.toEntity(seatRequest);
        seat.setId(id);

        Seat saved = screeningService.updateSeat(seat);

        return ResponseEntity.ok(SeatResponse.fromEntity(saved));
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> deleteSeatById(@PathVariable Integer id) {
        screeningService.deleteSeatById(id);

        return ResponseEntity.noContent().build();
    }


}

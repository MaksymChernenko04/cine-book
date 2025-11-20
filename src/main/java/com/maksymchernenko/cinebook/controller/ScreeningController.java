package com.maksymchernenko.cinebook.controller;

import com.maksymchernenko.cinebook.dto.ScreeningRequest;
import com.maksymchernenko.cinebook.dto.ScreeningResponse;
import com.maksymchernenko.cinebook.model.Screening;
import com.maksymchernenko.cinebook.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/screenings", produces = {"application/json", "application/xml"})
public class ScreeningController {

    private final ScreeningService screeningService;

    @Autowired
    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    // Screening endpoints

    @GetMapping
    public Page<ScreeningResponse> getAllScreenings(Pageable pageable,
                                                    @RequestParam(required = false) Integer screeningId,
                                                    @RequestParam(required = false) LocalDate date) {
        Page<Screening> page;
        if (screeningId != null && date != null) {
            page = screeningService.getScreeningsByMovieAndDate(screeningId, date, pageable);
        } else {
            page = screeningService.getAllScreenings(pageable);
        }

        return page.map(ScreeningResponse::fromEntity);
    }

    @GetMapping("/{id}")
    public ScreeningResponse getScreeningById(@PathVariable Integer id) {
        return ScreeningResponse.fromEntity(screeningService.getScreeningById(id));
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<ScreeningResponse> createScreening(@Valid @RequestBody ScreeningRequest screeningRequest) {
        Screening screening = ScreeningRequest.toEntity(screeningRequest);
        ScreeningResponse saved = ScreeningResponse.fromEntity(screeningService.createScreening(screening));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<ScreeningResponse> updateScreening(@PathVariable Integer id,
                                                             @Valid @RequestBody ScreeningRequest screeningRequest) {
        Screening screening = ScreeningRequest.toEntity(screeningRequest);
        screening.setId(id);

        Screening saved = screeningService.updateScreening(screening);

        return ResponseEntity.ok(ScreeningResponse.fromEntity(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Integer id) {
        screeningService.deleteScreeningById(id);

        return ResponseEntity.noContent().build();
    }
}

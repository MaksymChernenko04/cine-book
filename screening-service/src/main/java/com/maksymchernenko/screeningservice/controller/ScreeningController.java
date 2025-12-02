package com.maksymchernenko.screeningservice.controller;

import com.maksymchernenko.screeningservice.dto.*;
import com.maksymchernenko.screeningservice.model.Screening;
import com.maksymchernenko.screeningservice.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/screenings", produces = {"application/json", "application/xml"})
public class ScreeningController {

    private final ScreeningService screeningService;

    @Autowired
    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    public Page<ScreeningResponse> getAllScreenings(Pageable pageable,
                                                    @RequestParam(required = false) Integer movieId,
                                                    @RequestParam(required = false) LocalDate date) {
        Page<Screening> page;
        if (movieId != null && date != null) {
            page = screeningService.getScreeningsByMovieAndDate(movieId, date, pageable);
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

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> deleteScreening(@PathVariable Integer id) {
        screeningService.deleteScreeningById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/seats")
    public List<ScreeningSeatResponse> getSeatsByScreeningAndLockToken(@PathVariable("id") Integer screeningId,
                                                                       @RequestParam String lockToken) {
        return screeningService.getSeatsByScreeningAndLockToken(screeningId, lockToken).stream()
                .map(ScreeningSeatResponse::fromEntity)
                .toList();
    }

    @PostMapping(value = "/{id}/seats/lock", consumes = {"application/json", "application/xml"})
    public ResponseEntity<LockSeatsResponse> lockSeats(@PathVariable("id") Integer screeningId,
                                                       @Valid @RequestBody LockSeatsRequest request) {
        String lockToken = screeningService.lockSeats(screeningId, request.getSeatIds());
        LockSeatsResponse response = new LockSeatsResponse(lockToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/seats/confirm", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> confirmSeats(@PathVariable("id") Integer screeningId,
                                             @Valid @RequestBody LockTokenRequest request) {
        screeningService.confirmSeats(screeningId, request.getLockToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/seats/unlock", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> unlockSeats(@PathVariable("id") Integer screeningId,
                                            @Valid @RequestBody LockTokenRequest request) {
        screeningService.unlockSeats(screeningId, request.getLockToken());
        return ResponseEntity.noContent().build();
    }
}

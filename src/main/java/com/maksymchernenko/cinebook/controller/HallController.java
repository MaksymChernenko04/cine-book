package com.maksymchernenko.cinebook.controller;

import com.maksymchernenko.cinebook.dto.seat.AddSeatRequest;
import com.maksymchernenko.cinebook.dto.hall.HallRequest;
import com.maksymchernenko.cinebook.dto.hall.HallResponse;
import com.maksymchernenko.cinebook.model.Hall;
import com.maksymchernenko.cinebook.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/halls", produces = {"application/json", "application/xml"})
public class HallController {

    private final ScreeningService screeningService;

    @Autowired
    public HallController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/by-screening/{screeningId}")
    public HallResponse getHallByScreeningId(@PathVariable Integer screeningId) {
        return HallResponse.fromEntity(screeningService.getHallByScreeningId(screeningId));
    }

    @GetMapping("/{id}")
    public HallResponse getHallById(@PathVariable Integer id) {
        return HallResponse.fromEntity(screeningService.getHallById(id));
    }

    @GetMapping
    public Page<HallResponse> getAllHalls(Pageable pageable) {
        return screeningService.getAllHalls(pageable).map(HallResponse::fromEntity);
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<HallResponse> createHall(@Valid @RequestBody HallRequest hallRequest) {
        Hall hall = HallRequest.toEntity(hallRequest);
        HallResponse saved = HallResponse.fromEntity(screeningService.createHall(hall));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<HallResponse> updateHall(@PathVariable Integer id,
                                                   @Valid @RequestBody HallRequest hallRequest) {
        Hall hall = HallRequest.toEntity(hallRequest);
        hall.setId(id);

        Hall saved = screeningService.updateHall(hall);

        return ResponseEntity.ok(HallResponse.fromEntity(saved));
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> deleteHallById(@PathVariable Integer id) {
        screeningService.deleteHallById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{hallId}/seats", consumes = {"application/json", "application/xml"})
    public ResponseEntity<HallResponse> addSeatToHall(@PathVariable Integer hallId,
                                                      @Valid @RequestBody AddSeatRequest request) {
        Integer seatId = request.getSeatId();
        Hall saved = screeningService.addSeatByHallId(hallId, seatId);

        return ResponseEntity.ok(HallResponse.fromEntity(saved));
    }

    @DeleteMapping(value = "/{hallId}/seats/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> removeSeatFromHall(@PathVariable Integer hallId,
                                                   @PathVariable Integer id) {
        screeningService.removeSeatByHallId(hallId, id);

        return ResponseEntity.noContent().build();
    }
}

package com.maksymchernenko.bookingservice.controller;

import com.maksymchernenko.bookingservice.dto.BookingRequest;
import com.maksymchernenko.bookingservice.dto.BookingResponse;
import com.maksymchernenko.bookingservice.dto.CheckoutRequest;
import com.maksymchernenko.bookingservice.model.Booking;
import com.maksymchernenko.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/bookings", produces = {"application/json", "application/xml"})
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable Integer id) {
        Booking booking = bookingService.getBookingById(id);

        return BookingResponse.fromEntity(booking);
    }

    @GetMapping
    public Page<BookingResponse> getBookings(Pageable pageable,
                                             @RequestParam(required = false) Integer userId) {
        Page<Booking> page;
        if (userId != null) {
            page = bookingService.getBookingsByUserId(userId, pageable);
        } else {
            page = bookingService.getAllBookings(pageable);
        }

        return page.map(BookingResponse::fromEntity);
    }

    @PostMapping(consumes = {"application/json", "application/xml"})
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest.getUserId(), bookingRequest.getScreeningId(), bookingRequest.getSeatIds());
        BookingResponse response = BookingResponse.fromEntity(booking);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping(value = "/{id}/checkout", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> checkoutBooking(@PathVariable Integer id,
                                                @Valid @RequestBody CheckoutRequest checkoutRequest) {
        bookingService.checkoutBooking(id, checkoutRequest.getPaymentMethod());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json", "application/xml"})
    public ResponseEntity<Void> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBookingById(id);

        return ResponseEntity.noContent().build();
    }
}
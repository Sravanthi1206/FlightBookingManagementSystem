package com.flightapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.BookingResponseDTO;
import com.flightapp.model.Booking;
import com.flightapp.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/flight")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/booking/{flightid}")
    public ResponseEntity<BookingResponseDTO> bookTicket(
            @PathVariable("flightid") Long flightId,
            @Valid @RequestBody BookingRequestDTO request) {

        Booking booking = service.bookTicket(flightId, request);
        return ResponseEntity.ok(new BookingResponseDTO(booking.getPnr(), "Booking successful"));
    }


    @GetMapping("/ticket/{pnr}")
    public ResponseEntity<Booking> getTicket(@PathVariable String pnr) {
        return ResponseEntity.ok(service.getByPnr(pnr));
    }

    @GetMapping("/booking/history/{emailId}")
    public ResponseEntity<List<com.flightapp.model.Booking>> history(@PathVariable("emailId") String email) {
        return ResponseEntity.ok(service.getHistory(email));
    }

    @DeleteMapping("/booking/cancel/{pnr}")
    public ResponseEntity<String> cancel(@PathVariable String pnr) {
        service.cancel(pnr);
        return ResponseEntity.ok("Cancelled");
    }
}

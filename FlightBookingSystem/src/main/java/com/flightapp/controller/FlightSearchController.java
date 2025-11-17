package com.flightapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.dto.FlightSearchRequestDTO;
import com.flightapp.model.FlightSchedule;
import com.flightapp.service.FlightService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/flight")
public class FlightSearchController {
    private final FlightService flightService;

    public FlightSearchController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightSchedule>> search(@Valid @RequestBody FlightSearchRequestDTO request) {
        return ResponseEntity.ok(flightService.searchFlights(
            request.getFromPlace(), request.getToPlace(), request.getTravelDate()));
    }

}

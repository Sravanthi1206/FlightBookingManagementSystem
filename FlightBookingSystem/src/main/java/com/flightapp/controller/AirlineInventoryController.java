package com.flightapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.dto.AddInventoryDTO;
import com.flightapp.model.Airline;
import com.flightapp.model.FlightSchedule;
import com.flightapp.service.AirlineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/flight/airline")
public class AirlineInventoryController {

    private final AirlineService service;

    public AirlineInventoryController(AirlineService service) {
        this.service = service;
    }

    @PostMapping("/inventory/add")
    public ResponseEntity<FlightSchedule> addInventory(
            @RequestParam Long airlineId,
            @Valid @RequestBody AddInventoryDTO request) {

        FlightSchedule saved = service.addInventory(airlineId, request);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/add")
    public ResponseEntity<Airline> addAirline(@Valid @RequestBody Airline airline) {
        return ResponseEntity.ok(service.addAirline(airline));
    }
}



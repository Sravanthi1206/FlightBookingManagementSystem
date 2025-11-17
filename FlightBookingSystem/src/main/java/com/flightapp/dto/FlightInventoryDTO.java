package com.flightapp.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FlightInventoryDTO {
    private Long id;
    private String flightNumber;
    private String fromPlace;
    private String toPlace;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double price;
}

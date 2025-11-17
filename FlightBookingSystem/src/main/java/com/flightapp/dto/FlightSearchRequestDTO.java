package com.flightapp.dto;

import java.time.LocalDate;

import com.flightapp.enums.TripType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class FlightSearchRequestDTO {

    @NotBlank(message = "Source cannot be empty")
    private String fromPlace;

    @NotBlank(message = "Destination cannot be empty")
    private String toPlace;

    @NotNull(message = "Travel date is required")
    @FutureOrPresent(message = "Travel date cannot be in the past")
    private LocalDate travelDate;

    @NotNull(message = "Trip type is required")
    private TripType tripType;
}

package com.flightapp.dto;

import com.flightapp.enums.SeatType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class PassengerDTO {

    @NotBlank(message = "Passenger name is required")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;
}


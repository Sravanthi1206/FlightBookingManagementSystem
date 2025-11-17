package com.flightapp.dto;

import java.util.List;

import com.flightapp.enums.MealType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class BookingRequestDTO {

    @NotBlank(message = "Booker name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @Size(min = 1, message = "At least one passenger must be added")
    @Valid
    private List<PassengerDTO> passengers;
}

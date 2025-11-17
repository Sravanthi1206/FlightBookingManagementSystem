package com.flightapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.model.FlightSchedule;

public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {
    List<FlightSchedule> findByFromPlaceAndToPlaceAndDepartureTimeBetween(
        String fromPlace, String toPlace, LocalDateTime start, LocalDateTime end);
}

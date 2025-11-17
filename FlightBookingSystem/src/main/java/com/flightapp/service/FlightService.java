package com.flightapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flightapp.model.FlightSchedule;
import com.flightapp.repository.FlightScheduleRepository;

@Service
public class FlightService {
    private final FlightScheduleRepository repo;

    public FlightService(FlightScheduleRepository repo) {
        this.repo = repo;
    }

    public FlightSchedule addSchedule(FlightSchedule schedule) {
        return repo.save(schedule);
    }

    public List<FlightSchedule> searchFlights(String from, String to, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return repo.findByFromPlaceAndToPlaceAndDepartureTimeBetween(from, to, start, end);
    }

    public FlightSchedule findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Flight not found: " + id));
    }
}

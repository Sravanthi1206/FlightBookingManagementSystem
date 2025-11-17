package com.flightapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flightapp.model.FlightSchedule;
import com.flightapp.repository.FlightScheduleRepository;

class FlightServiceTest {

    @Mock
    private FlightScheduleRepository repo;

    @InjectMocks
    private FlightService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFlights() {
        LocalDate date = LocalDate.of(2025, 11, 20);
        FlightSchedule fs = new FlightSchedule();
        fs.setId(1L);
        fs.setFlightNumber("6E-501");

        when(repo.findByFromPlaceAndToPlaceAndDepartureTimeBetween(
                eq("Hyderabad"), eq("Bengaluru"),
                any(), any()
        )).thenReturn(List.of(fs));

        var results = service.searchFlights("Hyderabad", "Bengaluru", date);
        assertEquals(1, results.size());
        assertEquals("6E-501", results.get(0).getFlightNumber());
    }

    @Test
    void testAddSchedule() {
        FlightSchedule schedule = new FlightSchedule();
        schedule.setFlightNumber("TA-100");
        when(repo.save(schedule)).thenReturn(schedule);
        var saved = service.addSchedule(schedule);
        assertEquals("TA-100", saved.getFlightNumber());
    }
}

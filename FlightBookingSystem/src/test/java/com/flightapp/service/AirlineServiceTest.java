package com.flightapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flightapp.model.Airline;
import com.flightapp.model.FlightSchedule;
import com.flightapp.repository.AirlineRepository;
import com.flightapp.repository.FlightScheduleRepository;

class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepo;

    @Mock
    private FlightScheduleRepository scheduleRepo;

    @InjectMocks
    private AirlineService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAirline_New() {
        Airline a = new Airline();
        a.setName("Indigo");

        when(airlineRepo.findByName("Indigo")).thenReturn(Optional.empty());
        when(airlineRepo.save(a)).thenReturn(a);

        Airline saved = service.addAirline(a);
        assertEquals("Indigo", saved.getName());
    }

    @Test
    void testAddInventory_attachesAirline() {
        Airline airline = new Airline(); airline.setId(2L);
        FlightSchedule fs = new FlightSchedule(); fs.setTotalSeats(100);

        when(airlineRepo.findById(2L)).thenReturn(Optional.of(airline));
        when(scheduleRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FlightSchedule saved = service.addInventory(2L, fs);
        assertEquals(100, saved.getTotalSeats());
        assertEquals(airline, saved.getAirline());
    }
}

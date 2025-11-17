package com.flightapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.dto.AddInventoryDTO;
import com.flightapp.model.Airline;
import com.flightapp.model.FlightSchedule;
import com.flightapp.service.AirlineService;

@WebMvcTest(controllers = AirlineInventoryController.class)
class AirlineInventoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AirlineService airlineService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void addAirline_returnsSaved() throws Exception {
        Airline a = new Airline();
        a.setId(1L);
        a.setName("Indigo");

        when(airlineService.addAirline(any(Airline.class))).thenReturn(a);

        mvc.perform(post("/api/v1.0/flight/airline/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(a)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Indigo"));

        verify(airlineService).addAirline(any(Airline.class));
    }

    @Test
    void addInventory_withDto_returnsSavedFlight() throws Exception {
        AddInventoryDTO dto = new AddInventoryDTO();
        dto.setFlightNumber("TA-100");
        dto.setFromPlace("A");
        dto.setToPlace("B");
        dto.setDepartureTime(LocalDateTime.now().plusDays(2));
        dto.setArrivalTime(LocalDateTime.now().plusDays(2).plusHours(1));
        dto.setBasePrice(1500);
        dto.setTotalSeats(120);
        dto.setAvailableSeats(0);

        FlightSchedule returned = new FlightSchedule();
        returned.setFlightNumber(dto.getFlightNumber());

        when(airlineService.addInventory(eq(1L), any(AddInventoryDTO.class))).thenReturn(returned);

        mvc.perform(post("/api/v1.0/flight/airline/inventory/add?airlineId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flightNumber").value("TA-100"));

        verify(airlineService).addInventory(eq(1L), any(AddInventoryDTO.class));
    }
}

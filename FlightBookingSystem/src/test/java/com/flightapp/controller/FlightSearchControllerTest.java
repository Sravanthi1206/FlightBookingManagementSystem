package com.flightapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.dto.FlightSearchRequestDTO;
import com.flightapp.model.FlightSchedule;
import com.flightapp.service.FlightService;

@WebMvcTest(controllers = FlightSearchController.class)
class FlightSearchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void search_returnsFlights() throws Exception {
        FlightSchedule fs = new FlightSchedule();
        fs.setId(5L);
        fs.setFlightNumber("F-MOCK");
        fs.setDepartureTime(LocalDateTime.now().plusDays(2));

        when(flightService.searchFlights(eq("HYD"), eq("BLR"), any(LocalDate.class)))
                .thenReturn(List.of(fs));

        FlightSearchRequestDTO req = new FlightSearchRequestDTO();
        req.setFromPlace("HYD");
        req.setToPlace("BLR");
        req.setTravelDate(LocalDate.now().plusDays(2));
        req.setTripType(com.flightapp.enums.TripType.ONE_WAY);

        mvc.perform(post("/api/v1.0/flight/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].flightNumber").value("F-MOCK"));

        verify(flightService).searchFlights(eq("HYD"), eq("BLR"), any(LocalDate.class));
    }
}

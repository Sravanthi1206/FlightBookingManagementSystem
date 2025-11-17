package com.flightapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.enums.MealType;
import com.flightapp.enums.SeatType;
import com.flightapp.model.Booking;
import com.flightapp.model.FlightSchedule;
import com.flightapp.service.BookingService;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    private Booking sampleBooking;

    @BeforeEach
    void setup() {
        FlightSchedule fs = new FlightSchedule();
        fs.setId(10L);
        fs.setFlightNumber("FL-10");
        fs.setDepartureTime(LocalDateTime.now().plusDays(2));

        sampleBooking = new Booking();
        sampleBooking.setPnr("PNR-MOCK");
        sampleBooking.setFlight(fs);
        sampleBooking.setBookerEmail("a@b.com");
        sampleBooking.setSeatsBooked(1);
    }

    @Test
    void bookTicket_returnsPnr() throws Exception {
        when(bookingService.bookTicket(eq(10L), any(BookingRequestDTO.class))).thenReturn(sampleBooking);

        BookingRequestDTO req = new BookingRequestDTO();
        req.setName("B");
        req.setEmail("a@b.com");
        req.setMealType(MealType.VEG);
        PassengerDTO p = new PassengerDTO();
        p.setName("P1");
        p.setGender("M");
        p.setAge(30);
        p.setSeatNumber("1A");
        p.setSeatType(SeatType.WINDOW);
        req.setPassengers(List.of(p));

        mvc.perform(post("/api/v1.0/flight/booking/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnr").value("PNR-MOCK"))
            .andExpect(jsonPath("$.message").value("Booking successful"));

        verify(bookingService).bookTicket(eq(10L), any(BookingRequestDTO.class));
    }

    @Test
    void getTicket_returnsBooking() throws Exception {
        when(bookingService.getByPnr("PNR-MOCK")).thenReturn(sampleBooking);

        mvc.perform(get("/api/v1.0/flight/ticket/PNR-MOCK"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnr").value("PNR-MOCK"));

        verify(bookingService).getByPnr("PNR-MOCK");
    }

    @Test
    void cancel_callsService() throws Exception {
        doNothing().when(bookingService).cancel("PNR-MOCK");

        mvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR-MOCK"))
            .andExpect(status().isOk())
            .andExpect(content().string("Cancelled"));

        verify(bookingService).cancel("PNR-MOCK");
    }
}

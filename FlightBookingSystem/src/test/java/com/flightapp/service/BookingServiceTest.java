package com.flightapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.enums.MealType;
import com.flightapp.enums.SeatType;
import com.flightapp.exception.BadRequestException;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.model.Booking;
import com.flightapp.model.FlightSchedule;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightScheduleRepository;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightScheduleRepository flightRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTicketSuccessfully() {
        FlightSchedule flight = new FlightSchedule();
        flight.setId(10L);
        flight.setFlightNumber("6E-501");
        flight.setAvailableSeats(10);
        flight.setTotalSeats(10);
        flight.setBasePrice(2500.0);
        flight.setDepartureTime(LocalDateTime.now().plusDays(2));

        when(flightRepository.findById(10L)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(flightRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingRequestDTO req = new BookingRequestDTO();
        req.setName("Test User");
        req.setEmail("test@example.com");
        req.setMealType(MealType.VEG);

        PassengerDTO p = new PassengerDTO();
        p.setName("Passenger One");
        p.setGender("F");
        p.setAge(28);
        p.setSeatNumber("1A");
        p.setSeatType(SeatType.WINDOW);
        req.setPassengers(List.of(p));

        Booking booking = bookingService.bookTicket(10L, req);

        assertNotNull(booking.getPnr());
        assertEquals(1, booking.getSeatsBooked());
        assertEquals(9, flight.getAvailableSeats());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void testBookTicket_FlightNotFound() {
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());
        BookingRequestDTO req = new BookingRequestDTO();
        req.setPassengers(List.of());
        assertThrows(ResourceNotFoundException.class, () -> bookingService.bookTicket(999L, req));
    }

    @Test
    void testBookTicket_NotEnoughSeats() {
        FlightSchedule flight = new FlightSchedule();
        flight.setAvailableSeats(0);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        BookingRequestDTO req = new BookingRequestDTO();
        PassengerDTO p = new PassengerDTO();
        p.setSeatType(SeatType.AISLE);
        req.setPassengers(List.of(p));

        assertThrows(BadRequestException.class, () -> bookingService.bookTicket(1L, req));
    }

    @Test
    void testCancelBookingSuccess() {
        FlightSchedule fs = new FlightSchedule();
        fs.setAvailableSeats(5);
        fs.setDepartureTime(LocalDateTime.now().plusDays(3));

        Booking b = new Booking();
        b.setFlight(fs);
        b.setSeatsBooked(2);
        b.setCancelled(false);

        when(bookingRepository.findByPnr("ABC123")).thenReturn(Optional.of(b));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(flightRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        bookingService.cancel("ABC123");

        assertTrue(b.isCancelled());
        assertEquals(7, fs.getAvailableSeats());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void testCancelTooLate() {
        FlightSchedule fs = new FlightSchedule();
        fs.setDepartureTime(LocalDateTime.now().plusHours(10));
        Booking b = new Booking(); b.setFlight(fs); b.setCancelled(false);

        when(bookingRepository.findByPnr("XYZ123")).thenReturn(Optional.of(b));
        assertThrows(BadRequestException.class, () -> bookingService.cancel("XYZ123"));
    }
}

package com.flightapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightapp.dto.BookingRequestDTO;
import com.flightapp.exception.BadRequestException;
import com.flightapp.exception.ResourceNotFoundException;
import com.flightapp.model.Booking;
import com.flightapp.model.FlightSchedule;
import com.flightapp.model.Passenger;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightScheduleRepository;
import com.flightapp.util.PnrGenerator;
import com.flightapp.util.PricingUtil;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightScheduleRepository flightRepo;

    public BookingService(BookingRepository bookingRepository, FlightScheduleRepository flightRepo) {
        this.bookingRepository = bookingRepository;
        this.flightRepo = flightRepo;
    }

    @Transactional
    public Booking bookTicket(Long flightId, BookingRequestDTO request) {
        FlightSchedule flight = flightRepo.findById(flightId).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found: " + flightId));

        int seatsRequested = request.getPassengers().size();
        if (seatsRequested <= 0) {
			throw new BadRequestException("No passengers provided");
		}
        if (flight.getAvailableSeats() < seatsRequested) {
			throw new BadRequestException("Not enough seats available");
		}

        double total = PricingUtil.calculateTotal(flight, request.getPassengers());

        // reduce available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - seatsRequested);
        flightRepo.save(flight);

        Booking b = new Booking();
        b.setBookerName(request.getName());
        b.setBookerEmail(request.getEmail());
        b.setBookingTime(LocalDateTime.now());
        b.setFlight(flight);
        b.setSeatsBooked(seatsRequested);
        b.setTotalPrice(total);
        b.setPnr(PnrGenerator.generate(flight.getFlightNumber()));
        b.setMealType(request.getMealType());

        List<Passenger> passengers = request.getPassengers().stream().map(dto -> {
            Passenger p = new Passenger();
            p.setName(dto.getName());
            p.setAge(dto.getAge());
            p.setGender(dto.getGender());
            p.setSeatNumber(dto.getSeatNumber());
            p.setSeatType(dto.getSeatType());
            return p;
        }).collect(Collectors.toList());
        b.setPassengers(passengers);

        return bookingRepository.save(b);
    }

    public Booking getByPnr(String pnr) {
        return bookingRepository.findByPnr(pnr).orElseThrow(() -> new ResourceNotFoundException("PNR not found"));
    }

    public List<Booking> getHistory(String email) {
        return bookingRepository.findByBookerEmailOrderByBookingTimeDesc(email);
    }

    @Transactional
    public void cancel(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr).orElseThrow(() -> new ResourceNotFoundException("PNR not found"));
        if (booking.isCancelled()) {
			throw new BadRequestException("Already cancelled");
		}

        LocalDateTime departure = booking.getFlight().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(now, departure);
        if (diff.toHours() < 24) {
            throw new BadRequestException("Cancellation allowed only 24 hours before journey");
        }

        booking.setCancelled(true);
        booking.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // restore seats
        FlightSchedule flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsBooked());
        flightRepo.save(flight);
    }
}

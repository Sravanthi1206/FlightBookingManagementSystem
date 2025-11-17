package com.flightapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightapp.dto.AddInventoryDTO;
import com.flightapp.model.Airline;
import com.flightapp.model.FlightSchedule;
import com.flightapp.repository.AirlineRepository;
import com.flightapp.repository.FlightScheduleRepository;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;
    private final FlightScheduleRepository flightScheduleRepository;

    public AirlineService(AirlineRepository airlineRepository,
                          FlightScheduleRepository flightScheduleRepository) {
        this.airlineRepository = airlineRepository;
        this.flightScheduleRepository = flightScheduleRepository;
    }


    @Transactional
    public FlightSchedule addInventory(Long airlineId, AddInventoryDTO dto) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new RuntimeException("Airline not found: " + airlineId));

        FlightSchedule schedule = new FlightSchedule();
        schedule.setFlightNumber(dto.getFlightNumber());
        schedule.setFromPlace(dto.getFromPlace());
        schedule.setToPlace(dto.getToPlace());
        schedule.setDepartureTime(dto.getDepartureTime());
        schedule.setArrivalTime(dto.getArrivalTime());
        schedule.setBasePrice(dto.getBasePrice());
        schedule.setTotalSeats(dto.getTotalSeats());

        if (dto.getAvailableSeats() > 0) {
            schedule.setAvailableSeats(dto.getAvailableSeats());
        } else {
            schedule.setAvailableSeats(dto.getTotalSeats());
        }

        schedule.setAirline(airline);

        return flightScheduleRepository.save(schedule);
    }


    @Transactional
    public FlightSchedule addInventory(Long airlineId, FlightSchedule schedule) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new RuntimeException("Airline not found: " + airlineId));

        schedule.setAirline(airline);

        if (schedule.getAvailableSeats() <= 0) {
            schedule.setAvailableSeats(schedule.getTotalSeats());
        }

        return flightScheduleRepository.save(schedule);
    }



    public Airline addAirline(Airline airline) {
        Optional<Airline> existing = airlineRepository.findByName(airline.getName());
        return existing.orElseGet(() -> airlineRepository.save(airline));
    }
}

package com.flightapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.model.Airline;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findByName(String name);
}

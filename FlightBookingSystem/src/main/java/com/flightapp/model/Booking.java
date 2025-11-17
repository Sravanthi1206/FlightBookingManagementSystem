package com.flightapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.flightapp.enums.MealType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String pnr;

    private String bookerName;
    private String bookerEmail;

    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable=false)
    private FlightSchedule flight;

    private int seatsBooked;
    private double totalPrice;

    private boolean cancelled = false;
    private LocalDateTime cancelledAt;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "booking_id")
    private List<Passenger> passengers = new ArrayList<>();


}

package com.flightapp.util;

import java.util.List;

import com.flightapp.dto.PassengerDTO;
import com.flightapp.enums.SeatType;
import com.flightapp.model.FlightSchedule;

public class PricingUtil {
    private static final double WINDOW_PENALTY_PERCENT = 10.0; // 10% extra for window seat

    public static double calculateTotal(FlightSchedule flight, List<PassengerDTO> passengers) {
        double base = flight.getBasePrice();
        double total = 0.0;
        for (PassengerDTO p : passengers) {
            double seatPrice = base;
            if (p.getSeatType() == SeatType.WINDOW) {
                seatPrice += (base * WINDOW_PENALTY_PERCENT / 100.0);
            }

            total += seatPrice;
        }
        return total;
    }
}

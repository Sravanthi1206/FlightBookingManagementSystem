package com.flightapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PnrGenerator {
    private static final Random RAND = new Random();
    public static String generate(String flightNumber) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        int r = RAND.nextInt(900) + 100;
        return (flightNumber != null ? flightNumber.replaceAll("\\s+","").toUpperCase().substring(0, Math.min(3, flightNumber.length())) : "FLG")
                + time + r;
    }
}

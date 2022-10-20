package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static <T extends Comparable <T>> boolean isBetweenHalfOpen(T param, T startTime, T endTime) {
        return (startTime == null || param.compareTo(startTime) >= 0) && (endTime == null || param.compareTo(endTime) < 0);
    }
}


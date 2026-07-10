package com.expenses.tracker.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AppUtils {
    public static String getISTDateFormatted(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.of("Asia/Kolkata"))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}

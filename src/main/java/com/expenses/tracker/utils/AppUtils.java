package com.expenses.tracker.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AppUtils {

    /**
     * Formats a LocalDateTime object to IST (Indian Standard Time) format.
     *
     * @param dateTime The LocalDateTime object to be formatted.
     * @return The formatted date string in IST.
     */
    public static String getISTDateFormatted(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.of("Asia/Kolkata"))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}

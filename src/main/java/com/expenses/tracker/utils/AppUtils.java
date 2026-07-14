package com.expenses.tracker.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALL = UPPER + LOWER + DIGITS;


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

    /**
     * Generates a random password of the specified length. The password will contain at least one uppercase letter, one lowercase letter, and one digit.
     *
     * @param length Length
     * @return String
     */
    public static String generatePassword(int length) {
        // Implement your password generation logic here
        if (length < 3) {
            throw new IllegalArgumentException("Password length must be at least 3");
        }

        List<Character> password = new ArrayList<>();

        // Ensure at least one character from each category
        password.add(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.add(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        // Fill the remaining characters
        for (int i = 3; i < length; i++) {
            password.add(ALL.charAt(RANDOM.nextInt(ALL.length())));
        }

        // Shuffle to randomize positions
        Collections.shuffle(password, RANDOM);

        StringBuilder result = new StringBuilder();
        password.forEach(result::append);

        return result.toString();
    }
}

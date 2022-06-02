package com.slice.auto.utils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * Generates date string in {days} days after today or before today.
     * To increment current date use positive number, to decrement current date use negative number.
     * Ex. to get yesterdays date use 'getDateInDays(-1, "pattern")'
     *
     * @param days        number of days to increment/decrement
     * @param datePattern format pattern of returned date
     * @return date
     */
    public static String getDateInDays(int days, final String datePattern) {
        LocalDate date = LocalDate.now().plusDays(days);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(datePattern);

        return date.toString(dateTimeFormatter);
    }
}

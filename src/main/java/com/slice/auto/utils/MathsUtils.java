package com.slice.auto.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class MathsUtils {

    /**
     * Method allows to sum string numbers and to round the result.
     *
     * @param roundPlaces the number of chars after "."
     * @param strings     the number string to sum
     * @return sum result of the strings.
     */
    public static String sumIntStrings(int roundPlaces, String... strings) {
        if (roundPlaces < 0) throw new IllegalArgumentException("Illegal number of places to round.");

        double sumValue = Arrays.stream(strings).map(s -> s.replaceAll(",", "."))
                .map(s -> s.replaceAll("\\$", ""))
                .mapToDouble(Double::parseDouble).sum();

        BigDecimal bd = BigDecimal.valueOf(sumValue);
        bd = bd.setScale(roundPlaces, RoundingMode.HALF_UP);

        String pattern = String.format(".%sf", roundPlaces);
        return String.format("%" + pattern, bd.doubleValue());
    }

    /**
     * Method allows to multiply string numbers and to round the result.
     *
     * @param roundPlaces the number of chars after "."
     * @param strings     the number string to multiply
     * @return multiply result of the strings.
     */
    public static String multiplyIntStrings(int roundPlaces, String... strings) {
        if (roundPlaces < 0) throw new IllegalArgumentException("Illegal number of places to round.");

        BigDecimal multiplyValue = Arrays.stream(strings).map(s -> s.replaceAll(",", "."))
                .map(s -> s.replaceAll("\\$", ""))
                .map(s -> s.replaceAll("%", ""))
                .mapToDouble(Double::parseDouble)
                .mapToObj(BigDecimal::valueOf)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);

        String pattern = String.format(".%sf", roundPlaces);
        return String.format("%" + pattern, multiplyValue.setScale(roundPlaces, RoundingMode.HALF_UP).doubleValue());
    }
}

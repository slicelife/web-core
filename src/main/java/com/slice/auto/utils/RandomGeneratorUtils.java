package com.slice.auto.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomGeneratorUtils {

    private static final Faker faker = new Faker();

    /**
     * Generates a random email
     *
     * @return a random email
     */
    public static String generateRandomEmail() {
        return faker.name().username() + "@slice.dev";
    }

    /**
     * Generates a 10 digit (US) phone number. This may generate a phone number that is
     * technically not valid, despite matching the format.
     *
     * @return 10 digit phone number formatted like XXX555XXXX
     */
    public static String generateRandomPhoneNumber(boolean isFormatted) {
        String firstThreeDigits = generateRandomNumber(100, 999).toString();
        String lastFourDigits = generateRandomNumber(1000, 9990).toString();
        if (isFormatted) {
            return "(" + firstThreeDigits + ")" + " " + "555" + lastFourDigits;
        } else {
            return firstThreeDigits + "555" + lastFourDigits;
        }
    }

    /**
     * Generates a random string
     *
     * @return a random string
     */
    public static String generateRandomString(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    /**
     * Generates a random UUID
     *
     * @return a random UUID
     */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a random number in given range
     *
     * @return a random number
     */
    public static Integer generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

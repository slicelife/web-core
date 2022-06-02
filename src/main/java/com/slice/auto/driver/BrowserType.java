package com.slice.auto.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BrowserType {
    FIREFOX("Firefox"),
    CHROME("Chrome"),
    SAFARI("Safari");

    private String name;

    /**
     * Method to retrieve enum value from a string value.
     * Method is case insensitive.
     *
     * @param text string browser value, ex. "chrome"
     * @return enum value of browser type.
     */
    public static BrowserType fromValue(String text) {
        return Arrays.stream(BrowserType.values())
                .filter(browserType -> browserType.getName().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown browser type : " + text));
    }
}

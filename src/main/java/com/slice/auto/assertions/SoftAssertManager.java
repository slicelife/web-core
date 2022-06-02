package com.slice.auto.assertions;

import org.testng.asserts.SoftAssert;

public class SoftAssertManager {

    private static ThreadLocal<SoftAssert> threadLocal = new ThreadLocal<>();

    public static SoftAssert get() {
        return threadLocal.get();
    }

    public static void initialize() {
        threadLocal.set(new SoftAssert());
    }

    public static void assertAll() {
        if (SoftAssertManager.get() != null) {
            SoftAssertManager.get().assertAll();
            threadLocal.remove();
        }
    }
}

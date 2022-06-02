package com.slice.auto.driver;

import org.openqa.selenium.WebDriver;

public class WebDriverManager {

    /***
     * Threadlocal allows  to make a webdriver object thread-safe, i.e.
     * a single object can be used with multiply threads at the same time
     * without causing a problem.
     */
    private static ThreadLocal<WebDriver> threadLocal = new ThreadLocal<>();

    /***
     * The method gets the current thread’s copy of this thread-local variable to the specified value
     * @return webdriver of the current thread
     */
    public static WebDriver getDriver() {
        return threadLocal.get();
    }

    /***
     * The method sets the current thread’s copy of this thread-local variable to the specified value
     * @param driver set driver of the current thread
     */
    public static void setWebDriver(WebDriver driver) {
        threadLocal.set(driver);
    }

    /***
     * This method removes the current thread’s copy of this thread-local variable to the specified value
     */
    public static void quitDriver() {
        if (WebDriverManager.getDriver() != null) {
            WebDriverManager.getDriver().close();
            WebDriverManager.getDriver().quit();
            threadLocal.remove();
        }
    }
}

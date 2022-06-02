package com.slice.auto.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.concurrent.TimeUnit;

@Slf4j
public class WebDriverFactory {

    private static final int DEFAULT_WAIT_IN_SECONDS = 30;
    private static final int DEFAULT_IMPLICIT_WAIT_IN_SECONDS = 8;

    /***
     * Chooses a webdriver of a defined type
     * @param browser chosen driver
     */
    public static WebDriver getDriver(BrowserType browser) {
        WebDriver driver;
        switch (browser) {
            case SAFARI:
                driver = getSafari();
                break;
            case CHROME:
                driver = getChrome();
                break;
            case FIREFOX:
                driver = getFireFox();
                break;
            default:
                throw new RuntimeException("Incorrect BrowserName");
        }

        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT_IN_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_IN_SECONDS, TimeUnit.SECONDS);
        log.info("[{}] browser started!", browser.toString());
        return driver;
    }

    /**
     * Safari webdriver settings
     *
     * @return safari webdriver
     */
    private static SafariDriver getSafari() {
        WebDriverManager.getInstance(DriverManagerType.SAFARI).setup();
        return new SafariDriver();
    }

    /**
     * Firefox webdriver settings
     *
     * @return firefox webdriver
     */
    public static FirefoxDriver getFireFox() {
        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("disable-gpu");
        firefoxOptions.addArguments("--start-maximized");
        return new FirefoxDriver(firefoxOptions);
    }

    /***
     * Chrome webdriver settings
     * @return chrome webdriver
     */
    public static ChromeDriver getChrome() {
        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-proxy-server");

        if (Boolean.parseBoolean(System.getenv("INCOGNITO"))) {
            chromeOptions.addArguments("--incognito");
        }

        if (Boolean.parseBoolean(System.getenv("HEADLESS"))) {
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--window-size=1920,1080");
        }
        return new ChromeDriver(chromeOptions);
    }

    public static int getDefaultImplicitWait() {
        return DEFAULT_IMPLICIT_WAIT_IN_SECONDS;
    }
}

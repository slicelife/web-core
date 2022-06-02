package com.slice.auto.baseConfigurations;


import com.slice.auto.assertions.SoftAssertManager;
import com.slice.auto.driver.BrowserType;
import com.slice.auto.driver.WebDriverFactory;
import com.slice.auto.driver.WebDriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void init(@Optional("chrome") String browser) {
        WebDriverManager.setWebDriver(WebDriverFactory.getDriver(BrowserType.fromValue(browser)));
        SoftAssertManager.initialize();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriverManager.quitDriver();
    }
}

package com.slice.auto.baseConfigurations;

import com.slice.auto.driver.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage extends PageHelpersWeb {

    protected WebDriver driver;

    protected BasePage() {
        driver = WebDriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }
}

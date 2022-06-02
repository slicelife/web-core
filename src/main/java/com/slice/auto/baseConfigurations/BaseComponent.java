package com.slice.auto.baseConfigurations;

import com.slice.auto.driver.WebDriverManager;
import org.openqa.selenium.support.PageFactory;

public class BaseComponent extends PageHelpersWeb {

    protected BaseComponent() {
        PageFactory.initElements(WebDriverManager.getDriver(), this);
    }
}

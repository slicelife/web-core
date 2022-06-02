package com.slice.auto.utils;

import com.slice.auto.driver.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

@Slf4j
public class AttachUtils {

    /***
     * Generates a test run screenshot
     */
    public synchronized static void takeScreenshot(final String filePath) {
        File screenshotFile = ((TakesScreenshot) WebDriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(filePath);
        try {
            FileUtils.copyFile(screenshotFile, destinationFile);
        } catch (IOException e) {
            log.error("Unable to save screenshot: {}", e.getMessage());
        }
    }
}

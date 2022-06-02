package com.slice.auto.driver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.stream.Collectors;

public class WaitConditions {

    public static ExpectedCondition<Boolean> htmlToFinishLoading() {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String script = "return document.readyState";
                return ((JavascriptExecutor) driver).executeScript(script).equals("complete");
            }

            @Override
            public String toString() {
                return "Wait for html to finish loading";
            }
        };
    }

    public static ExpectedCondition<Boolean> ajaxToFinishLoading() {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String script = "return jQuery.active == 0 ||  jQuery.active == 1";
                return (Boolean) ((JavascriptExecutor) driver).executeScript(script);
            }

            @Override
            public String toString() {
                return "Wait for ajax to finish loading";
            }
        };
    }

    public static ExpectedCondition<Boolean> elementToDisappear(WebElement element) {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !element.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "Wait for element to disappear";
            }
        };
    }

    public static ExpectedCondition<Boolean> elementToDisappear(By by) {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !driver.findElement(by).isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "Wait for element to disappear";
            }
        };
    }

    public static ExpectedCondition<Boolean> elementsTextOrderToChange(List<String> previousOrder, By elementsLocator) {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<String> currentOrder = WebDriverManager.getDriver().findElements(elementsLocator).stream()
                            .map(WebElement::getText).collect(Collectors.toList());
                    return !currentOrder.equals(previousOrder);
                } catch (NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "Elements order has not changed";
            }
        };
    }

    public static ExpectedCondition<Boolean> elementListToChange(List<WebElement> previousOrder, By elementsLocator) {

        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> currentElements = WebDriverManager.getDriver().findElements(elementsLocator);
                    return !currentElements.equals(previousOrder);
                } catch (NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "Elements order has not changed";
            }
        };
    }
}

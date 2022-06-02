package com.slice.auto.baseConfigurations;


import com.slice.auto.constants.CommonConstants;
import com.slice.auto.driver.WaitConditions;
import com.slice.auto.driver.WebDriverFactory;
import com.slice.auto.driver.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageHelpersWeb {

    protected void selectDropdownOptionByPartialText(WebElement element, final String elementOption) {
        List<WebElement> options = new Select(element).getOptions();
        options.stream().filter(option -> option.getText().contains(elementOption)).findFirst().orElseThrow(() ->
                new NoSuchElementException(String.format("Option [%s] is not present in the dropdown", elementOption))).click();
    }

    protected String getDropDownSelectedOption(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText();
    }

    public void navigateToUrl(String URL) {
        WebDriverManager.getDriver().get(URL);
        waitForPageToLoad();
    }

    public String getCurrentURL() {
        waitForPageToLoad();
        return WebDriverManager.getDriver().getCurrentUrl();
    }

    protected boolean isNewWindowOpened(int windowsNumber) {
        int currentWindowsNumber = WebDriverManager.getDriver().getWindowHandles().size();
        return currentWindowsNumber > windowsNumber;
    }

    protected boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementPresent(int implicitWait, WebElement element) {
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
        boolean isPresent = isElementPresent(element);
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(WebDriverFactory.getDefaultImplicitWait(), TimeUnit.SECONDS);
        return isPresent;
    }

    protected boolean isButtonEnabled(WebElement element) {
        return element.getAttribute("aria-disabled").equals("false");
    }

    protected boolean isChecked(WebElement checkBox) {
        return (Boolean.parseBoolean(checkBox.getAttribute("checked")));
    }

    protected void refreshBrowser() {
        WebDriverManager.getDriver().navigate().refresh();
        waitForPageToLoad();
    }

    protected void waitForPageToLoad(int timeOut) {
        createWait(timeOut)
                .until(WaitConditions.htmlToFinishLoading());
    }

    protected void waitForAjax() {
        createWait(CommonConstants.globalTimeout)
                .until(WaitConditions.ajaxToFinishLoading());
    }

    protected void waitForPageToLoad() {
        waitForPageToLoad(CommonConstants.globalTimeout);
    }

    protected void waitForTextToDisappear(int timeout, WebElement element, String text) {
        createWait(timeout)
                .until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, text)));
    }

    protected void waitForTextToDisappear(WebElement element, String text) {
        waitForTextToDisappear(CommonConstants.globalTimeout, element, text);
    }

    protected void waitForTextToBePresentInElement(int timeout, WebElement element, String text) {
        createWait(timeout)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    protected void waitForTextToBePresentInElement(WebElement element, String text) {
        waitForTextToBePresentInElement(CommonConstants.globalTimeout, element, text);
    }

    protected WebElement waitForElementToBecomeVisible(int timeout, WebElement element) {
        return createWait(timeout)
                .until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForElementToBecomeVisible(WebElement element) {
        return waitForElementToBecomeVisible(CommonConstants.globalTimeout, element);
    }

    protected WebElement waitForVisibilityByLocator(int timeout, By by) {
        return createWait(timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitForVisibilityByLocator(By by) {
        return waitForVisibilityByLocator(45, by);
    }

    protected WebElement waitForElementToBecomeClickableBy(By by) {
        return waitForElementToBecomeClickable(WebDriverManager.getDriver().findElement(by));
    }

    protected WebElement waitForElementToBecomeClickable(int timeout, WebElement element) {
        return createWait(timeout)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitForElementToBecomeClickable(WebElement element) {
        return waitForElementToBecomeClickable(CommonConstants.globalTimeout, element);
    }

    protected void waitForAttributePresence(WebElement element, String attribute, String strValue) {
        createWait(CommonConstants.stripeTimeout)
                .until(ExpectedConditions.attributeToBe(element, attribute, strValue));
    }

    protected void waitForCheckboxStateToBe(WebElement element, String state) {
        createWait(CommonConstants.globalTimeout)
                .until(ExpectedConditions.attributeToBe(element, "data-checked", state));
    }

    protected void waitForButtonToBeEnabled(WebElement element) {
        createWait(CommonConstants.globalTimeout)
                .until(ExpectedConditions.attributeToBe(element, "aria-disabled", "false"));
    }

    protected void waitUntilUrlContains(int timeout, String urlFragment) {
        createWait(timeout)
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    protected void waitUntilElementDisappear(int timeout, final WebElement element) {
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        createWait(timeout)
                .until(WaitConditions.elementToDisappear(element));
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(WebDriverFactory.getDefaultImplicitWait(), TimeUnit.SECONDS);
    }

    protected void waitUntilElementDisappear(final WebElement element) {
        waitUntilElementDisappear(CommonConstants.globalTimeout, element);
    }

    protected void waitUntilElementDisappearBy(int timeOut, final By by) {
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        createWait(timeOut)
                .until(WaitConditions.elementToDisappear(by));
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(WebDriverFactory.getDefaultImplicitWait(), TimeUnit.SECONDS);
    }

    protected void waitUntilElementDisappearBy(final By by) {
        waitUntilElementDisappearBy(CommonConstants.globalTimeout, by);
    }

    protected void waitForAlert(int timeOut) {
        createWait(timeOut)
                .until(ExpectedConditions.alertIsPresent());
    }

    protected void waitForElementsListChangeByText(int timeOut, List<String> previousElementsList, By elementLocator) {
        createWait(timeOut)
                .until(WaitConditions.elementsTextOrderToChange(previousElementsList, elementLocator));
    }

    protected void waitForElementsListChangeByText(List<String> previousElementsList, By elementLocator) {
        waitForElementsListChangeByText(CommonConstants.globalTimeout, previousElementsList, elementLocator);
    }

    protected void waitForElementsListChangeBy(List<WebElement> previousElementsList, By elementLocator) {
        waitForElementsListChangeBy(CommonConstants.globalTimeout, previousElementsList, elementLocator);
    }

    protected void waitForElementsListChangeBy(int timeOut, List<WebElement> previousElementsList, By elementLocator) {
        createWait(timeOut)
                .until(WaitConditions.elementListToChange(previousElementsList, elementLocator));
    }

    protected void waitForAlert() {
        waitForAlert(CommonConstants.globalTimeout);
    }

    protected void waitForNewWindow(int currentWindowsNumber) {
        createWait(CommonConstants.globalTimeout)
                .until(ExpectedConditions.numberOfWindowsToBe(currentWindowsNumber + 1));
    }

    protected void navigateBrowserBack() {
        WebDriverManager.getDriver().navigate().back();
    }

    @SneakyThrows
    protected String getUrlContents(String link) {
        StringBuilder content = new StringBuilder();
        URL url = new URL(link);
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }

        bufferedReader.close();
        return content.toString();
    }

    protected List<String> getTextForEachElementInParent(WebElement parentContainer, String requestedElement) {
        List<WebElement> allElements = parentContainer.findElements(By.xpath(requestedElement));
        return allElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    protected List<String> getAttributeForEachElementInParent(WebElement parentContainer, String requestedElement, String requestedAttribute) {
        List<WebElement> allElements = parentContainer.findElements(By.xpath(requestedElement));
        return allElements.stream().map(element -> element.getAttribute(requestedAttribute)).collect(Collectors.toList());
    }

    protected String getCheckboxState(WebElement element) {
        return element.getAttribute("data-checked");
    }

    protected String getElementValue(WebElement element) {
        return element.getAttribute("value");
    }

    @SneakyThrows
    protected void sendKeysInterval(WebElement element, String str, int interval) {
        for (char symbol : str.toCharArray()) {
            element.sendKeys(String.valueOf(symbol));
            Thread.sleep(interval);
        }
    }

    protected void sendKeysInterval(WebElement element, String str) {
        sendKeysInterval(element, str, CommonConstants.inputInterval);
    }

    protected void sendKeysReactTriggerChange(WebElement element, String str) {
        this.waitForPageToLoad();
        JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
        element.sendKeys(str);
        js.executeScript("reactTriggerChange(arguments[0]);", element);
    }

    protected void clearAndSendKeys(WebElement element, String keysToSend) {
        element.clear();
        element.sendKeys(keysToSend);
    }

    protected void sendKeysAndPressTab(WebElement element, String keysToSend) {
        element.clear();
        element.sendKeys(keysToSend);
        element.sendKeys(Keys.TAB);
    }

    protected void clearNumberOfCharacters(WebElement element, int noOfChars) {
        element.sendKeys(Keys.chord(Keys.COMMAND, Keys.END));
        IntStream.range(0, noOfChars).forEach(i -> element.sendKeys(Keys.BACK_SPACE));
    }

    protected boolean doAllElementsInListContainValue(By by, String value) {
        return WebDriverManager.getDriver().findElements(by).stream().allMatch(el -> el.getText().contains(value));
    }

    protected void clickJs(WebElement element) {
        ((JavascriptExecutor) WebDriverManager.getDriver()).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoElementsLeft(WebElement element) {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("arguments[0].scrollRight = arguments[0].offsetWidth", element);
    }

    protected void scrollIntoElementsRight(WebElement element) {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("arguments[0].scrollLeft = arguments[0].offsetWidth", element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoViewIfNeeded(true);", element);
    }

    protected void scrollDocumentHeight() {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollToPoint(String x, String y) {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript(String.format("scroll(%s, %s);", x, y));
    }

    protected void executeUrlContents(String url) {
        ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript(getUrlContents(url));
    }

    private FluentWait<WebDriver> createWait(int timeout) {
        return new WebDriverWait(WebDriverManager.getDriver(), timeout)
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
}

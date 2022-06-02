package com.slice.auto.testrail;

import com.slice.auto.CreateXmlFile;
import com.slice.auto.utils.AttachUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
public class TRListener implements ITestListener {

    private static final int PASS = 1;
    private static final int FAIL = 5;
    private static final int RETEST = 4;
    private String TestID;
    private Method testMethod;
    private TrIntegration testRail;

    public Method setTestMethod(ITestResult result) {
        IClass object = result.getTestClass();
        Class<?> newObject = object.getRealClass();
        try {
            testMethod = newObject.getMethod(result.getName());
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return testMethod;
    }

    public String setTestId() {
        UseAsTestRailId useAsTestName = testMethod.getAnnotation(UseAsTestRailId.class);
        TestID = Integer.toString(useAsTestName.testRailId());
        return TestID;
    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (setTestMethod(result).isAnnotationPresent(UseAsTestRailId.class)) {
            if (Boolean.parseBoolean(System.getenv("TESTRAIL_UPDATE"))) {
                testRail = new TrIntegration(System.getenv("TESTRAIL_URL"), System.getenv("TESTRAIL_USERNAME"), System.getenv("TESTRAIL_PASSWORD"));
                try {
                    testRail.updateTestStatus(System.getenv("TESTRAIL_RUNID"), setTestId(), PASS, "PASS");
                } catch (NumberFormatException | IOException | APIException e) {
                    e.printStackTrace();
                    System.out.println("\u001B[33m" + "\nTestrail not updated for ==> " + result.getTestClass().getName() + " => " + result.getName() + "\n" + "\u001B[0m");
                }
            }
        }

        System.out.println("\u001B[32m" + "\n==> " + result.getTestClass().getName() + " => " + result.getName() + " passed!\n" + "\u001B[0m");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (setTestMethod(result).isAnnotationPresent(UseAsTestRailId.class)) {
            if (Boolean.parseBoolean(System.getenv("TESTRAIL_UPDATE"))) {
                testRail = new TrIntegration(System.getenv("TESTRAIL_URL"), System.getenv("TESTRAIL_USERNAME"), System.getenv("TESTRAIL_PASSWORD"));
                try {
                    testRail.updateTestStatus(System.getenv("TESTRAIL_RUNID"), setTestId(), FAIL, result.getThrowable().getMessage());
                } catch (NumberFormatException | IOException | APIException e) {
                    e.printStackTrace();
                    System.out.println("\u001B[33m" + "\nTestrail not updated for ==> " + result.getTestClass().getName() + " => " + result.getName() + "\n" + "\u001B[0m");
                }
            }
        }

        String className = result.getTestClass().getName();
        String methodName = result.getName();
        String os = className.contains("ios") ? "IOS" : className.contains("android") ? "ANDROID" : "SLICEOS";
        CreateXmlFile.createTestElement(className, methodName, os);

        System.out.println("\u001B[31m" + "\n==> " + result.getTestClass().getName() + " => " + result.getName() + " failed!\n" + "\u001B[0m");
        System.out.println("******************ERROR*******************");
        System.out.println(result.getThrowable().getMessage());
        System.out.println("******************ERROR******************");

        try {
            if (Boolean.parseBoolean(System.getenv("TESTRAIL_UPDATE"))) {
                final String screenshotPath = "target/screenshots/" + result.getName() + "-" + LocalDateTime.now() + ".png";
                AttachUtils.takeScreenshot(screenshotPath);
                testRail.addAttachmentToResult(System.getenv("TESTRAIL_RUNID"), TestID, screenshotPath);
            }
        } catch (IOException | APIException e) {
            log.error("Unable to take a screenshot or attach it to Test Rail");
        } catch (Exception e) {
            log.error("Unable to take a screenshot or attach it to Test Rail");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (setTestMethod(result).isAnnotationPresent(UseAsTestRailId.class)) {
            if (Boolean.parseBoolean(System.getenv("TESTRAIL_UPDATE"))) {
                testRail = new TrIntegration(System.getenv("TESTRAIL_URL"), System.getenv("TESTRAIL_USERNAME"), System.getenv("TESTRAIL_PASSWORD"));
                try {
                    testRail.updateTestStatus(System.getenv("TESTRAIL_RUNID"), setTestId(), RETEST, "Retrying test after failure.");
                } catch (NumberFormatException | IOException | APIException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\u001B[33m" + "\n==> " + result.getTestClass().getName() + " => " + result.getName() + " skipped! Retrying... \n" + "\u001B[0m");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}


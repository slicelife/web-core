package com.slice.auto.listeners;

import com.slice.auto.constants.CommonConstants;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {

    private int actualRetry = 0;
    private static final int MAX_RETRY = System.getenv("RETRY") == null ? CommonConstants.defaultRetryCount : Integer.parseInt(System.getenv("RETRY"));

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (actualRetry < MAX_RETRY) {
                actualRetry++;
                iTestResult.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }
}
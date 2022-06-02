package com.slice.auto.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/***
 * IAnnotationTransformer is a TestNG listener which allows you to modify TestNG annotations
 * and configure them further during runtime.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /***
     * Transform method is called for every test during the test run.
     * Adds retry for every failed test.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzerImpl.class);
    }
}
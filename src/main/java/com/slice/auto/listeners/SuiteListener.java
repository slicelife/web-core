package com.slice.auto.listeners;

import com.slice.auto.CreateXmlFile;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        System.setProperty("isXmlDynamic", "" + suite.getXmlSuite().getFileName().contains(CreateXmlFile.fileName) + "");
        CreateXmlFile.createXmlDocument();
        CreateXmlFile.createSuiteElement();
    }

    @Override
    public void onFinish(ISuite suite) {
        CreateXmlFile.createXmlFile();
    }
}

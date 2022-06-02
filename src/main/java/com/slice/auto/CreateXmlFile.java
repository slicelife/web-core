package com.slice.auto;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class CreateXmlFile {

    public static final String fileName = "dynamicSuite.xml";
    public static final String xmlFilePath = System.getProperty("user.dir") + "/" + fileName;
    public static Document document;
    public static Element suiteTag;

    public static void createXmlDocument() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document =  documentBuilder.newDocument();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    public static void createSuiteElement() {
        suiteTag = document.createElement("suite");
        Attr suiteName = document.createAttribute("name");
        suiteName.setValue("WebDriver");
        suiteTag.setAttributeNode(suiteName);
        document.appendChild(suiteTag);

        Element listenersTag = document.createElement("listeners");
        Element listenerTagTestrail = document.createElement("listener");
        Attr listenerClassTestrail = document.createAttribute("class-name");
        listenerClassTestrail.setValue("com.core.testrail.TRListener");
        listenerTagTestrail.setAttributeNode(listenerClassTestrail);
        listenersTag.appendChild(listenerTagTestrail);

        Element listenerTagSuite = document.createElement("listener");
        Attr listenerClassSuite = document.createAttribute("class-name");
        listenerClassSuite.setValue("com.core.listeners.SuiteListener");
        listenerTagSuite.setAttributeNode(listenerClassSuite);
        listenersTag.appendChild(listenerTagSuite);
        suiteTag.appendChild(listenersTag);
    }

    public static void createTestElement(String className, String methodName, String os) {
        Element testTag = document.createElement("test");
        Attr testName = document.createAttribute("name");
        testName.setValue(methodName);
        testTag.setAttributeNode(testName);

        Element parameterTag = document.createElement("parameter");
        Attr parameterName = document.createAttribute("name");
        parameterName.setValue("os");
        Attr parameterValue = document.createAttribute("value");
        parameterTag.setAttributeNode(parameterName);
        parameterTag.setAttributeNode(parameterValue);
        parameterValue.setValue(os);
        testTag.appendChild(parameterTag);

        Element classesTag = document.createElement("classes");
        Element classTag = document.createElement("class");
        Attr classNameAttr = document.createAttribute("name");
        classNameAttr.setValue(className);
        classTag.setAttributeNode(classNameAttr);

        Element methodsTag = document.createElement("methods");
        Element includeTag = document.createElement("include");
        Attr includeName = document.createAttribute("name");
        includeName.setValue(methodName);
        includeTag.setAttributeNode(includeName);

        methodsTag.appendChild(includeTag);
        classTag.appendChild(methodsTag);
        classesTag.appendChild(classTag);
        testTag.appendChild(classesTag);
        suiteTag.appendChild(testTag);
    }

    public static void createXmlFile() {
        try {
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}




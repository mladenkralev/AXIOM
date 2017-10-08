package com.test;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXBuilder;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by MLKR on 07/10/2017.
 */
public class TestAxiom {
    private static ClassLoader classLoader;
    private static File xmlFile;
    private static XMLStreamReader streamReader;

    @BeforeClass
    public static void setUp() throws FileNotFoundException, XMLStreamException {
        classLoader = TestAxiom.class.getClassLoader();

        xmlFile = new File(classLoader.
                getResource("fileToBeRead.xml").getFile());
        streamReader = XMLInputFactory.newInstance().
                createXMLStreamReader(new FileInputStream(xmlFile));

        System.out.println("Parser class" + streamReader.getClass());

    }

    @AfterClass
    public static void clean() throws XMLStreamException {
        streamReader.close();
    }

    @Test
    public void testReading() {
        StAXOMBuilder builder = new StAXOMBuilder(streamReader);
        //get the root element (in this case the envelope)
        OMElement documentElement = builder.getDocumentElement();

        System.out.println(documentElement);

    }

    @Test
    public void testWriting() throws XMLStreamException, IOException {
        String xmlString = "<book>" + "<name>Qucik-start Axis</name>" + "<isbn>978-1-84719-286-8</isbn>" + "</book>";

        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());
        //create a builder. Since you want the XML as a plain XML, you can
        //just use the plain OMBuilder
        StAXBuilder builder = new StAXOMBuilder(xmlStream);
        //return the root element.
        String pathRootString = System.getProperty("user.dir");
        File outputFile = new File(String.valueOf(Paths.get(pathRootString) + File.separator + "temp"));
        outputFile.createNewFile();

        assertTrue(outputFile.exists());
        System.out.println("File location is " + outputFile.getAbsolutePath());

        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.append( builder.getDocumentElement().toString());
        fileWriter.close();
    }
}

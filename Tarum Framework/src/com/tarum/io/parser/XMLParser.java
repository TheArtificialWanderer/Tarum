package com.tarum.io.parser;

import com.tarum.io.content.type.*;
import com.tarum.util.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLParser extends ContentParser {

    private Logger logger;
    private XMLDocument xmlDocument;

    private boolean debugMode = true;

    public Logger getLogger() {
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public XMLDocument getContentContainer() {
        return xmlDocument;
    }
    public void setContentContainer(XMLDocument xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    public XMLDocument getXMLDocument(){
        return this.xmlDocument;
    }
    public void setXMLDocument (XMLDocument xmlDocument){
        this.xmlDocument = xmlDocument;
    }
    public boolean isDebugModeEnabled() {
        return debugMode;
    }
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public XMLParser(){
    }
    public XMLParser (File file){
        super (file);

        init();
    }
    public XMLParser (ContentContainer container){
        this.xmlDocument = createXMLDocument(container);

        init();
    }

    private void init(){
        if (getXMLDocument() == null){
            this.xmlDocument = new XMLDocument();
        }
    }

    public XMLDocument createXMLDocument(){
        return createXMLDocument(getFile());
    }
    public XMLDocument createXMLDocument(File file){
        if (file == null) return null;

        this.xmlDocument = new XMLDocument(file);

        return getXMLDocument();
    }
    public XMLDocument createXMLDocument (BasicContentContainer container) {
        System.out.println("createXMLDocument (BasicContentContainer)");
        this.xmlDocument = new XMLDocument();

        if (container == null){
            return xmlDocument;
        }

        // Identify the type of ContentContainer so that we may properly
        // create an XML document and populate it accordingly.
        Class parentClass = container.getClass().getSuperclass();

        // Retrieve all of the fields in the ContentContainer class
        // so that we may retrieve the data stored field by field.
        Field[] fields = container.getClass().getDeclaredFields();

        if (fields == null){
            return xmlDocument;
        }

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(container);

                // Create a new ContentField instance to store the class field information
                // from the associated ContentContainer instance.
                ContentField contentField = new ContentField(xmlDocument, field.getName());

                // Determine if the Field is a Map type, if so we'll iterate through
                // it's entries and add each entry to our XML document appropriately.
                if (value instanceof Map<?, ?>) {
                    contentField.setFieldType(ContentField.FieldType.HashMap);

                    HashMap entryMap = (HashMap) value;

                    Iterator i = entryMap.entrySet().iterator();

                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();

                        Object entryKey = entry.getKey();
                        Object entryValue = entry.getValue();

                        // TODO: *****
                    }

                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return xmlDocument;
    }

    public XMLDocument loadFile (){
        return loadFile (getFile());
    }
    public XMLDocument loadFile (File file){
        if (file != null && file.exists()){
            setFile (file);
        }

        if (getFile() == null || !getFile().exists()){
            return null;
        }

        return this.xmlDocument = parseFileContent();
    }

    public XMLDocument parseFileContent(){
        return parseFileContent(getFile());
    }
    public XMLDocument parseFileContent (File file) {
        if (file != null){
            setFile(file);
        }

        if (getFile() == null && getXMLDocument() == null || !getFile().exists() && getXMLDocument() == null) return null;

        if (getXMLDocument() == null){
            this.xmlDocument = createXMLDocument(getFile());
        }

        Document doc = xmlDocument.getDocument();

        Element element = doc.getDocumentElement();

        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++){
            Node n = nodeList.item(i);

            NamedNodeMap attributeMap = n.getAttributes();

            for (int j = 0; j < attributeMap.getLength(); j++){
                Node attributeNode = attributeMap.item(j);

            }
        }

        return this.getXMLDocument();
    }

    public boolean isValidXMLFile (File file){
        // TODO: *******
        return true;
    }

}
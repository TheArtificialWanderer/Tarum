package com.tarum.io.parser;

import com.tarum.io.content.type.*;
import com.tarum.util.GlueList;
import com.tarum.util.IOUtils;
import com.tarum.util.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

        // TODO: CHECK IF CONTENTCONTAINER INSTANCE ASSOCIATED FILE IS A VALID XML FILE,
        // IF SO LOAD THE FILE FROM THE LOCAL FILESYSTEM INTO A XMLDOCUMENT CONTAINER

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
//                ContentField contentField = new ContentField(xmlDocument, field.getName());

                // Determine if the Field is a Map type, if so we'll iterate through
                // it's entries and add each entry to our XML document appropriately.
                if (value instanceof Map<?, ?>) {
//                    contentField.setFieldType(ContentField.FieldType.HashMap);
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "Map");

                    HashMap entryMap = (HashMap) value;
                    Iterator i = entryMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry<?,?> entry = (Map.Entry<?,?>) i.next();

                        Object entryKey = entry.getKey();
                        Object entryValue = entry.getValue();
                        String serializedKey = null, serializedValue = null;

                        if (entryKey instanceof String){
                            serializedKey = String.valueOf(entryKey);
                        } else if (entryKey instanceof Integer){
                            int temp = (Integer) entryKey;
                            serializedKey = String.valueOf(entryValue);
                        } else if (entryKey instanceof Long){
                            serializedKey = String.valueOf(entryKey);
                        } else if (entryKey instanceof byte[]){
                            serializedKey = new String((byte[]) entryValue);
                        } else if (entryKey instanceof Object){
                            serializedKey = IOUtils.SerializeObjectToString(entryValue);
                        } else {
                            serializedKey = (String) entryKey;
                        }
                        if (entryValue instanceof String){
                            serializedValue = (String) entryValue;
                        } else if (entryValue instanceof Integer){
                            serializedValue = String.valueOf(entryValue);
                        } else if (entryValue instanceof Long){
                            serializedValue = String.valueOf(entryValue);
                        } else if (entryValue instanceof byte[]){
                            serializedValue = new String((byte[]) entryValue);
                        } else if (entryValue instanceof Object){
                            serializedValue = IOUtils.SerializeObjectToString(entryValue);
                        } else {
                            serializedValue = (String) entryValue;
                        }

                        Element subElement = (Element) XMLDocument.createNode(xmlDocument.getDocument(), field.getName()+"Entry", serializedValue);
                        subElement.setAttribute("key", serializedKey);
                        element.appendChild(subElement);
                    }

                } else if (value instanceof List){
                    GlueList list = (GlueList) value;
                    Iterator i = list.iterator();
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "List");

                    while (i.hasNext()){
                        Object object = i.next();
                        String serializedValue = null;

                        if (object instanceof String){
                            serializedValue = (String) object;
                        } else if (object instanceof Integer){
                            serializedValue = String.valueOf(object);
                        } else if (object instanceof Long){
                            serializedValue = String.valueOf(object);
                        } else if (object instanceof byte[]){
                            serializedValue = new String((byte[]) object);
                        } else if (object instanceof Object){
                            serializedValue = IOUtils.SerializeObjectToString(object);
                        } else {
                            serializedValue = (String) object;
                        }
                    }
                } else if (value instanceof byte[]){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "byte[]");
                    element.setTextContent(new String((byte[]) value));
                    xmlDocument.getDocument().getDocumentElement().appendChild(element);
                } else if (value instanceof String[]){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "String[]");
                    for (int i = 0; i < ((String[])value).length; i++){
                        Node n = XMLDocument.createNode(xmlDocument.getDocument(), String.valueOf(i), ((String[])value)[i]);
                        element.appendChild(n);
                    }
                    xmlDocument.getDocument().appendChild(element);
                } else if (value instanceof int[]){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "int[]");
                    for (int i = 0; i < ((int[])value).length; i++){
                        int val = ((int[])value)[i];
                        Node n = XMLDocument.createNode(xmlDocument.getDocument(), String.valueOf(i), String.valueOf(val));
                        element.appendChild(n);
                    }
                    xmlDocument.getDocument().appendChild(element);
                } else if (value instanceof String){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "String");
                    element.setTextContent((String)value);
                } else if (value instanceof Integer){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "Integer");
                    element.setTextContent(String.valueOf((int)value));
                } else if (value instanceof Long){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "Long");
                    element.setTextContent(String.valueOf((Long)value));
                } else if (value instanceof char[]){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "char[]");
                    element.setTextContent(new String((char[])value));
                } else if (value instanceof Boolean){
                    Element element = xmlDocument.getDocument().createElement(field.getName());
                    element.setAttribute("type", "boolean");
                    element.setTextContent(Boolean.toString((boolean)value));
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
        return this.getXMLDocument();
    }

    public static final boolean isValidXMLFile (File file){
        if (file == null || !file.exists()) return false;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String ln = null;
            while ((ln = reader.readLine()) != null){
                if (ln.replaceAll(" ","").startsWith("<?xml")){
                    reader.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
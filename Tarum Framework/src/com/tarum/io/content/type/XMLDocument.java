package com.tarum.io.content.type;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLDocument extends ContentContainer implements Serializable {

    private Document document;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    private NodeList activeNodeList;

    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
    public DocumentBuilderFactory getDocumentBuilderFactory() {
        return documentBuilderFactory;
    }
    public void setDocumentBuilderFactory(DocumentBuilderFactory documentBuilderFactory) {
        this.documentBuilderFactory = documentBuilderFactory;
    }
    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }
    public void setDocumentBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }
    public NodeList getActiveNodeList() {
        return activeNodeList;
    }
    public void setActiveNodeList(NodeList activeNodeList) {
        this.activeNodeList = activeNodeList;
    }

    public XMLDocument(){
        this(null);
    }
    public XMLDocument(File file){
        super(file);

        initialize();
    }

    private void initialize(){
        if (documentBuilderFactory == null){
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            this.document = documentBuilder.newDocument();

            String rootElementName = getFile() == null ? "Root_Element" : getFile().getName();
            Element rootElement = document.createElement(rootElementName);

            document.appendChild(rootElement);

            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    public static String encodeXML(String str) {
        if (str == null) return null;
        return str.replaceAll("&", "QQwwQQ")
                .replaceAll("<", "WWeeWW")
                .replaceAll(">", "EErrEE")
                .replaceAll("'", "RRttRR")
                .replaceAll("\"", "TTyyTT")
                .replaceAll("=", "YYuuYY");
//                .replaceAll("*", "UUiiUU")
//                .replaceAll(String.valueOf('['), "IIooII")
//                .replaceAll(String.valueOf(']'), "OOppOO")
//                .replaceAll(String.valueOf('#'), "AAssAA")
//                .replaceAll(String.valueOf('-'), "SSddSS");
    }
    public static String decodeXML (String str){
        if (str == null) return null;
        return str.replaceAll("QQwwQQ", "&")
                .replaceAll("WWeeWW", "<")
                .replaceAll("EErrEE", ">")
                .replaceAll("RRttRR", "'")
                .replaceAll("TTyyTT", "\"")
                .replaceAll("YYuuYY", "=");
//                .replaceAll("UUiiUU", "*")
//                .replaceAll("IIooII", String.valueOf('['))
//                .replaceAll("OOppOO", String.valueOf(']'))
//                .replaceAll("AAssAA", String.valueOf('#'))
//                .replaceAll("SSddSS", String.valueOf('-'));
    }

    /**
     * TODO: *******
     * @param doc - The target XML document container
     * @param fieldMap - A HashMap containing the names and values of each field
     *                 the Node will have.
     * @return
     */
    public static Node createNode(Document doc, String name, HashMap<String,String> fieldMap) {
        Element element = doc.createElement(name);
        Iterator i = fieldMap.entrySet().iterator();

        while (i.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry<String,String>) i.next();

//            StringEscapeUtils
            element.appendChild(createNodeElement(doc, entry.getKey(), entry.getValue()));
            // user.setAttribute("id", id);
        }

        doc.getDocumentElement().appendChild(element);
        return element;
    }

    /**
     * TODO: *************
     * @param doc
     * @param name
     * @param value
     * @return
     */
    public static Node createNodeElement(Document doc, String name,
                                          String value) {
        System.out.println("encode_xml: " + encodeXML(name));
        Element node = doc.createElement(encodeXML(name));
        node.appendChild(doc.createTextNode(encodeXML(value)));
        return node;
    }

    public boolean export (){
        return export (getFile());
    }
    public boolean export (File file) {
        return performContentExportation(file);
    }
    @Override
    public boolean performContentExportation (File file){
        if (file != null){
            setFile(file);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        DOMSource source = new DOMSource(document);
        StreamResult result = null;

        File dir = new File (getFile().getParent());
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {

            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
            result = new StreamResult(new FileOutputStream(getFile()));
            transformer.transform(source, result);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}

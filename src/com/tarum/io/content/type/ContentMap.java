package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.io.parser.XMLParser;
import com.tarum.util.IOUtils;
import com.tarum.util.MathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContentMap extends BasicContentContainer implements Serializable {

    private transient HashMap<Long, Object> dataMap;
    private transient HashMap<Object, Long> keyMap;

    private Class dataType = Object.class;
    private Class keyType = String.class;

    public HashMap<Long, Object> getDataMap() {
        return dataMap;
    }
    public void setDataMap(HashMap<Long, Object> dataMap) {
        this.dataMap = dataMap;
    }
    public HashMap<Object, Long> getKeyMap() {
        return keyMap;
    }
    public void setKeyMap(HashMap<Object, Long> keyMap) {
        this.keyMap = keyMap;
    }
    public Class getDataType(){
        return this.dataType;
    }
    public void setDataType (Class dataType){
        this.dataType = dataType;
    }
    public Class getKeyType(){
        return this.keyType;
    }
    public boolean setKeyType (Class keyType){
        if (keyType == null) return false;
        this.keyType = keyType;
        return true;
    }

    public ContentMap (){
        this(new HashMap<Long,Object>());
    }
    public ContentMap (ContentManager contentManager){
        super(contentManager);

        init();
    }
    public ContentMap (File file){
        super(file);

        init();
    }
    public ContentMap (HashMap<Long, Object> dataMap){
        this(dataMap, null);
    }
    public ContentMap (HashMap<Long, Object> dataMap, HashMap<Object, Long> keyMap){
        this.dataMap = dataMap != null ? dataMap : new HashMap<>();
        this.keyMap = keyMap != null ? keyMap : new HashMap<>();

        init();
    }

    private void init(){
        if (getDataMap() == null) {
            this.dataMap = new HashMap<>();
        }
        if (getKeyMap() == null) {
            this.keyMap = new HashMap<>();
        }
    }

    public boolean contains (long key){
        return dataMap.containsKey(key);
    }
    public Object get (long key){
        if (key <= 0 || !dataMap.containsKey(key)){
            return null;
        }
        return dataMap.get(key);
    }
    public Object get (Object keyMapIdentifier){
        if (keyMapIdentifier == null) return null;
        if (!keyMap.containsKey(keyMapIdentifier)){
            return null;
        }
        return get(keyMap.get(keyMapIdentifier));
    }
    public Object getKeyByName (String keyName){
        if (keyName == null) return null;
        if (!keyMap.containsKey(keyName)) return null;
        return keyMap.get(keyName);
    }
    public long add (Object value){
        long key = MathUtils.GenerateUID();
        if (!add(key, value)){
            return 0;
        }
        return key;
    }
    public boolean add (long key, Object value){
        return add(key, value, null);
    }
    public long add (Object keyMapIdentifier, Object value){
        if (value == null) return 0;

        long key = MathUtils.GenerateUID();

        if (add(key, value, keyMapIdentifier)){
            return key;
        } else {
            return 0;
        }
    }
    public boolean add (long key, Object value, Object keyMapIdentifier){
        if (value == null) return false;

        if (key <= 0){
            key = MathUtils.GenerateUID();
        }

        dataMap.put(key, value);

        if (keyMapIdentifier != null){
            keyMap.put(keyMapIdentifier, key);
        }
        return true;
    }
    public boolean addKey (long entryKey, Object associatedKey){
        if (associatedKey == null) return false;
        if (!dataMap.containsKey(entryKey)){
            return false;
        }
        keyMap.put(associatedKey, entryKey);
        return true;
    }

    @Override
    public boolean performContentImportation(File file){
        if (file != null && getFile().getAbsolutePath() != file.getAbsolutePath()){
            setFile(file);
        }

        getContentManager().logLine("ContentMap: Performing content importation (file_path:" + file.getAbsolutePath() + ")..");

        XMLParser parser = new XMLParser(getFile());
        XMLDocument xmlDocument = parser.loadFile();

        if (xmlDocument != null){
            Document doc = xmlDocument.getDocument();

            // TODO: CHANGE HOW WE SEARCH FOR ELEMENTS, USE THE FIELD NAME AND X,Y,Z
            NodeList nodeList = doc.getElementsByTagName("dataMapEntry");
            for (int i = 0; i < nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);

                String id = element.getAttribute("id");
                String val = element.getTextContent();

                Object object = IOUtils.DeserializeObject(val);

                if (object == null){
                    continue;
                }

                dataMap.put(Long.valueOf(id), object);
            }

            nodeList = doc.getElementsByTagName("keyMapEntry");
            for (int i = 0; i < nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);

                String id = element.getAttribute("id");
                String val = element.getNodeValue();
                Object object = IOUtils.DeserializeObject(val);

                if (object == null){
                    continue;
                }

                keyMap.put(object, Long.valueOf(id));
            }
        }

        return true;
    }
    /**
     * TODO *****
     * @param file
     * @return
     */
    @Override
    public boolean performContentExportation (File file){
        if (file != null){
            setFile(file);
        }

        System.out.println("ContentMap.performContentExportation: Preparing XML document for exportation..");

        XMLParser xmlParser = new XMLParser(getFile());
        XMLDocument xmlDocument = xmlParser.createXMLDocument(this);
        Document doc = xmlDocument.getDocument();
        Element root = null;
        Iterator i = null;

        /**
         * Iterate through our 'dataMap' and convert the entries into
         * a format that can be stored in our XML document we're building
         */
        if (dataMap != null && !dataMap.isEmpty()){
            i = dataMap.entrySet().iterator();

            System.out.println("ContentMap.performContentExportation: Creating XML elements for " +
                    "all entries within our 'dataMap'...");

//            HashMap<String, String> fieldMap = new HashMap();

            Element dataMapNode = doc.createElement("dataMap");
            doc.getDocumentElement().appendChild(dataMapNode);
            while (i.hasNext()){
                Map.Entry<Long, Object> entry = (Map.Entry<Long,Object>) i.next();

                long entryKey = entry.getKey();
                Object entryValue = entry.getValue();
                Element element = doc.createElement("dataMapEntry");

                System.out.println("Creating DataMap entry in XML document..");
//                Node key = doc.createTextNode(String.valueOf(entryKey));
//                element.appendChild(key);
                element.setAttribute("key", String.valueOf(entryKey));

                if (entryValue instanceof String){
                    element.setAttribute("serialization", "false");
                    Node n = doc.createTextNode((String)entryValue);
                    element.appendChild(n);
//                    Node node = XMLDocument.createNode(doc, String.valueOf(entryKey), String.valueOf(entryValue));
//                    root.appendChild(node);
                } else if (entryValue instanceof Integer){
                    element.setAttribute("serialization", "false");
                    Node n = doc.createTextNode(String.valueOf(entryValue));
                    element.appendChild(n);
//                    root.setAttribute("serialization", "false");
//                    Node node = XMLDocument.createNode(doc, String.valueOf(entryKey), String.valueOf(entryValue));
//                    root.appendChild(node);
                } else if (entryValue instanceof Long){
                    element.setAttribute("serialization", "false");
                    Node n = doc.createTextNode(String.valueOf(entryValue));
                    element.appendChild(n);
//                    root.setAttribute("serialization", "false");
//                    Node node = XMLDocument.createNode(doc, String.valueOf(entryKey), String.valueOf(entryValue));
//                    root.appendChild(node);
                } else if (entryValue instanceof Object){
                    element.setAttribute("serialization", "false");
                    String serialized = IOUtils.SerializeObjectToString(entryValue);
                    Node n = doc.createTextNode(serialized);
                    element.appendChild(n);
//                    root.setAttribute("serialization", "true");
//                    String serialized = IOUtils.SerializeObjectToString(entryValue);
//                    Node node = XMLDocument.createNode(doc, String.valueOf(entryKey), serialized);
//                    root.appendChild(node);
                }

                dataMapNode.appendChild(element);
            }

        }

        /**
         * Iterate through our 'keyMap' and convert the entries into
         * a format that can be stored in our XML document we're building
         */
        if (keyMap != null && !keyMap.isEmpty()){
            i = keyMap.entrySet().iterator();

//            HashMap<String,String> fieldMap = new HashMap();

            Element keyMapNode = doc.createElement("keyMap");
            doc.getDocumentElement().appendChild(keyMapNode);
            while (i.hasNext()){
                Map.Entry<Object, Long> entry = (Map.Entry<Object, Long>) i.next();

                Object entryKey = entry.getKey();
                long entryValue = entry.getValue();

                // TODO: ** XML NODE NAMES CANNOT HAVE SPECIAL CHARACTERS,
                // TODO: REPLACE ALL SPECIAL CHARACTERS GENERATED FROM SERIALIZATION
                // TODO: WITH SOMETHING ACCEPTABLE.
//                fieldMap.put(IOUtils.SerializeObjectToString(entryKey), String.valueOf(entryValue));
//                root = doc.createElement("Element_Test");
//                Element item = doc.createElement("XML_Element_Test");
//                root.appendChild(item);

                System.out.println("Creating keymap entry (key:" + IOUtils.SerializeObjectToString(entryKey) + ", value: " + entryValue + ") in XML document..");

                Element element = doc.createElement("keyMapEntry");
                element.setAttribute("key", IOUtils.SerializeObjectToString(entryKey));
//                Node key = doc.createTextNode(IOUtils.SerializeObjectToString(entryKey));
//                element.appendChild(key);
                Node value = doc.createTextNode(String.valueOf(entryValue));
                element.appendChild(value);
                keyMapNode.appendChild(element);
            }

//            Node node = XMLDocument.createNode(doc, "keymapEntry", fieldMap);
        }

        xmlDocument.setDocument(doc);

        if (getContentManager().getLogger() != null) {
            getContentManager().getLogger().log("ContentMap.performContentExportation(File): " +
                    "Successfully created XML document (file_path: " + file.getAbsolutePath());
        }

        if (xmlDocument.export(file)){

            if (getContentManager().getLogger() != null) {
                getContentManager().getLogger().log("ContentMap.performContentExportation(File): " +
                        "Successfully exported XML document to the local filesystem (file_path: " + file.getAbsolutePath() + ")!");
            }
            return true;
        } else {
            if (getContentManager().getLogger() != null) {
                getContentManager().getLogger().logError("ContentMap.performContentExportation(File): " +
                        "Failed to export XML document to the local filesystem (file_path: " + file.getAbsolutePath() + ")!");
            }
            return false;
        }
    }

}

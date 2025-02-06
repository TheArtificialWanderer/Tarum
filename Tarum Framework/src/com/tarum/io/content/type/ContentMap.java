package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.io.parser.XMLParser;
import com.tarum.util.IOUtils;
import com.tarum.util.MathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

    private transient Object keyType = String.class;

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
    public Object getKeyType(){
        return this.keyType;
    }
    public boolean setKeyType (Object keyType){
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

//        System.out.println("ContentMap.performContentExportation(File)");

        XMLParser xmlParser = new XMLParser(getFile());
        XMLDocument xmlDocument = xmlParser.createXMLDocument(this);
        Document doc = xmlDocument.getDocument();
        Element root = null;

        /**
         * Iterate through our 'dataMap' and convert the entries into
         * a format that can be stored in our XML document we're building
         */
        Iterator i = null;
        if (dataMap != null && !dataMap.isEmpty()){
            i = dataMap.entrySet().iterator();

            System.out.println("ContentMap.performContentExportation: Creating XML elements for " +
                    "all entries within our 'dataMap'...");

            while (i.hasNext()){
                Map.Entry<Long, Object> entry = (Map.Entry<Long,Object>) i.next();

                long entryKey = entry.getKey();
                Object entryValue = entry.getValue();

                //TODO: *****
                root = doc.createElement("map_entry");
                Node n = doc.createTextNode(String.valueOf(entryKey));
                root.appendChild(n);

                if (entryValue instanceof String){
                    root.setAttribute("serialization", "false");
                } else if (entryValue instanceof Integer){
                    root.setAttribute("serialization", "false");
                } else if (entryValue instanceof Long){
                    root.setAttribute("serialization", "false");
                } else if (entryValue instanceof Object){
                    root.setAttribute("serialization", "true");
                    String serialized = IOUtils.SerializeObjectToString(entryValue);
                    n = doc.createTextNode(serialized);
                    root.appendChild(n);
                }

            }
        }

        /**
         * Iterate through our 'keyMap' and convert the entries into
         * a format that can be stored in our XML document we're building
         */
        if (keyMap != null && !keyMap.isEmpty()){
            i = keyMap.entrySet().iterator();

            HashMap<String,String> fieldMap = new HashMap();
            while (i.hasNext()){
                Map.Entry<Object, Long> entry = (Map.Entry<Object, Long>) i.next();

                Object entryKey = entry.getKey();
                long entryValue = entry.getValue();

                // TODO: ** XML NODE NAMES CANNOT HAVE SPECIAL CHARACTERS,
                // TODO: REPLACE ALL SPECIAL CHARACTERS GENERATED FROM SERIALIZATION
                // TODO: WITH SOMETHING ACCEPTABLE.
//                System.out.println(IOUtils.SerializeObjectToString(entryKey));
                fieldMap.put(IOUtils.SerializeObjectToString(entryKey), String.valueOf(entryValue));
                Node node = XMLDocument.createNode(doc, "keymapEntry", fieldMap);

                // TODO: ******
//                root = doc.createElement("Element_Test");
//                Element item = doc.createElement("XML_Element_Test");
//                root.appendChild(item);
            }
        }

        xmlDocument.setDocument(doc);

        System.out.println("ContentMap.performContentExportation(File): Created XML document successfully!");

        return xmlDocument.export(file);
    }

}

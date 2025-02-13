package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.util.GlueList;
import com.tarum.util.IOUtils;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: ******************
 */
public class ContentFormat extends BasicContentContainer implements Serializable {

    private transient HashMap<Long, String> fieldMap;
    private transient HashMap<String, Long> keyMap;

    public static final String DEFAULT_FILE_EXTENSION = ".dat";

    public static enum Type{
        XML_DOCUMENT, UML_DOCUMENT
    }

    public HashMap getFieldMap() {
        return fieldMap;
    }
    public void setFieldMap(HashMap fieldMap) {
        this.fieldMap = fieldMap;
    }
    public String getFieldValue (String fieldName){
        if (fieldName == null) return null;
        return getFieldValue(keyMap.get(fieldName));
    }
    public String getFieldValue (long fieldUID){
        if (fieldMap == null || fieldMap.isEmpty()) return null;
        if (!fieldMap.containsKey(fieldUID)) return null;
        return fieldMap.get(fieldUID);
    }
    public HashMap getKeyMap() {
        return keyMap;
    }
    public void setKeyMap(HashMap keyMap) {
        this.keyMap = keyMap;
    }
    public String getKeyIdentifier (long fieldUID){
        if (keyMap == null || keyMap.isEmpty()) return null;

        for (Map.Entry<String, Long> entry : keyMap.entrySet()){
            if (entry.getValue().equals(fieldUID)){
                return entry.getKey();
            }
        }
        return null;
    }

    public ContentFormat (ContentManager contentManager){
        this (contentManager, null);
    }
    public ContentFormat(ContentManager contentManager, File file) {
        super (contentManager, file);

        if (getFile() == null){
            setFile(generateFile());
        }
    }

    public File generateFile(){
        String fileName = "content_format";
        String filePath = getContentManager().getContentContainerDirectory()+"content_format/";
        File dir = new File (filePath);

        if (!dir.exists()){
            dir.mkdirs();
        } else {
            int fileCount = dir.listFiles().length;

            if (fileCount > 0){
                fileName += "_" + fileCount;
            }
        }

        fileName += DEFAULT_FILE_EXTENSION;
        return new File (filePath + fileName);
    }

    public boolean loadFileContent (File file){
        if (file == null || !file.exists()) return false;

        GlueList<String> content = IOUtils.LoadFileContent(file);

        if (content.isEmpty()){
            return false;
        }

        return false;
    }

}

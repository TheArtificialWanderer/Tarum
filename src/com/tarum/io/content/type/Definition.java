package com.tarum.io.content.type;

import com.tarum.io.content.DefinitionManager;

import java.io.File;
import java.io.Serializable;

public class Definition extends BasicContentContainer implements Serializable {

    private transient DefinitionManager definitionManager;
    private transient ContentMap fieldMap;

    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
    public void setDefinitionManager(DefinitionManager definitionManager) {
        this.definitionManager = definitionManager;
    }
    public ContentMap getFieldMap() {
        return fieldMap;
    }
    public void setFieldMap(ContentMap fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Definition(DefinitionManager definitionManager) {
        this(definitionManager, "Definition");
    }
    public Definition(DefinitionManager definitionManager, String name) {
        super(definitionManager != null ? definitionManager.getContentManager() : null);
        this.definitionManager = definitionManager;
        setName(name == null ? "Definition" : name);

        initialize();
    }
    public Definition(DefinitionManager definitionManager, File file) {
        super(definitionManager != null ? definitionManager.getContentManager() : null, file);
        this.definitionManager = definitionManager;
        if (file != null) {
            this.setFile(file);
        }
        this.setName(file != null ? file.getName().split(".")[0] : "Definition");

        initialize();
    }

    private void initialize(){
        fieldMap = new ContentMap(definitionManager.getContentManager());
        fieldMap.setDataType(ContentField.class);

        definitionManager.addDefinition(this);
    }

    public ContentField getField (long fieldUID){
        if (!fieldMap.contains(fieldUID)) return null;
        return (ContentField) fieldMap.get(fieldUID);
    }
    public ContentField getFieldByName (String fieldName){
        long uid = (Long) fieldMap.getKeyByName(fieldName);
        return getField(uid);
    }
    public long addField (ContentField contentField){
        if (contentField == null) return 0;
        long uid = fieldMap.add(contentField);
        return uid;
    }

}

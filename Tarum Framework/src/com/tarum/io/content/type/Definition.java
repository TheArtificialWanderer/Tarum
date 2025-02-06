package com.tarum.io.content.type;

import com.tarum.io.content.DefinitionManager;

import java.io.File;
import java.io.Serializable;

public class Definition extends BasicContentContainer implements Serializable {

    private DefinitionManager definitionManager;
    private ContentMap fieldMap;

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
        super(definitionManager != null ? definitionManager.getContentManager() : null);
        this.definitionManager = definitionManager;
    }
    public Definition(DefinitionManager definitionManager, String name) {
        super(definitionManager != null ? definitionManager.getContentManager() : null);
        this.definitionManager = definitionManager;
        this.setName(name);
        definitionManager.addDefinition(this);
    }
    public Definition(DefinitionManager definitionManager, File file) {
        super(definitionManager != null ? definitionManager.getContentManager() : null, file);
        this.definitionManager = definitionManager;
        this.setFile(file);
    }

    public ContentField getField (int fieldId){
        return null;
    }
    public ContentField getField (String fieldName){
        return null;
    }



}

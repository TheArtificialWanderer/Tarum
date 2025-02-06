package com.tarum.io.content;

import com.tarum.io.Config;
import com.tarum.io.content.type.ContentMap;
import com.tarum.io.content.type.Definition;
import com.tarum.util.GlueList;
import com.tarum.util.IOUtils;

import java.io.File;

public class DefinitionManager {

    private ContentManager contentManager;
    private String contentDir;
    private Config config;
    private ContentMap definitionMap;

    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public String getContentDirectory() {
        return contentDir;
    }
    public void setContentDirectory(String contentDir) {
        this.contentDir = contentDir;
    }
    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
    public ContentMap getDefinitionMap() {
        return definitionMap;
    }
    public void setDefinitionMap(ContentMap definitionMap) {
        this.definitionMap = definitionMap;
    }

    public DefinitionManager(ContentManager contentManager){
        this(contentManager, null);
    }
    public DefinitionManager(String contentDir){
        this(null, contentDir);
    }
    public DefinitionManager(ContentManager contentManager, String contentDir){
        this.contentManager = contentManager == null ? new ContentManager() : contentManager;
        if (contentDir != null){
            this.contentDir = contentDir;
        } else {
            this.contentDir = contentManager.getDirectory()+"definitions/";
        }

        File dir = new File (getContentDirectory());
        if (!dir.exists()) dir.mkdir();

        initialize();
    }

    private void initialize(){
        File dir = new File (getContentDirectory());

        GlueList<File> definitionFiles = IOUtils.GetAllFilesRecursively(getContentDirectory());

        if (!definitionFiles.isEmpty()){
            for (File file : definitionFiles){

            }
        }
    }

    public long addDefinition (Definition definition){
        definitionMap.add(definition.getUID(), definition);
        return definition.getUID();
    }

    public Definition createDefinition (String name){
        Definition definition = new Definition(this, name);

        return definition;
    }

    public Definition loadDefinitionFile (File file){

        return null;
    }

    public boolean exportDefinitionFile (Definition definition){
        return exportDefinitionFile(definition, null);
    }
    public boolean exportDefinitionFile (Definition definition, File file){
        if (definition == null) return false;

        if (file != null){
            definition.setFile(file);
        }

        return false;
    }
}

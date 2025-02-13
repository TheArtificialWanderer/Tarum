package com.tarum.io.content.type;

import com.tarum.io.Configuration;
import com.tarum.io.content.ContentManager;

import java.io.Serializable;

public class Database extends BasicContentContainer implements Serializable {

    private Configuration configuration;
    private ContentMap contentMap;
    private ContentStoragePreferenceType contentStoragePreferenceType = ContentStoragePreferenceType.PREFER_CONTENT_METADATA_OPTIMIZATION;
    private EntryStorageType entryStorageType = EntryStorageType.INDIVIDUAL_FILES;

    /**
     * How to store database content
     */
    public enum EntryStorageType{
        SINGULAR_FILE, INDIVIDUAL_FILES
    }

    public enum ContentStoragePreferenceType{

        /**
         * By using this preference the Database will optimize content stored
         * within the database to conserve on memory and typically reduce
         * content-handling operation times (In cases where large amounts of metadata is
         * often times used this can have an adverse effect on performance)
         */
        PREFER_MEMORY_OPTIMIZATION,
        /**
         * By using this preference the Database will utilize methods to enrich
         * metadata associated with content stored within the database
         * for content-handling optimization purposes (Typically at the cost
         * of increased memory allocation and possibly increased content-handling
         * operation times)
         */
        PREFER_CONTENT_METADATA_OPTIMIZATION
    }

    public enum ContentStorageOptimizationPreference{
        PREFER_PERFORMANCE, PREFER_CONTENT_METADATA_QUALITY
    }

    public Configuration getConfiguration(){
        return this.configuration;
    }
    public void setConfiguration (Configuration config){
        this.configuration = config;
    }
    public ContentMap getContentMap() {
        return contentMap;
    }
    public void setContentMap(ContentMap contentMap) {
        this.contentMap = contentMap;
    }
    public ContentStoragePreferenceType getContentStoragePreferenceType() {
        return contentStoragePreferenceType;
    }
    public void setContentStoragePreferenceType(ContentStoragePreferenceType contentStoragePreferenceType) {
        this.contentStoragePreferenceType = contentStoragePreferenceType;
    }
    public EntryStorageType getEntryStorageType() {
        return entryStorageType;
    }
    public void setEntryStorageType(EntryStorageType entryStorageType) {
        this.entryStorageType = entryStorageType;
    }

    public Database(ContentManager contentManager) {
        this(contentManager, null);
    }
    public Database(ContentManager contentManager, String name) {
        super(contentManager);
        if (name != null){
            setName(name);
        } else {
            setName("Database");
        }

        setFilePath(contentManager.getDirectory()+"db/");
        setFileExtension(".db");

        initialize();
    }

    private void initialize(){

        /**
         * TODO: LOAD ONLY THE ESSENTIAL METADATA, THIS WILL HELP US TO PREPARE OUR CONTENTMAP SO
         * THAT WHEN CONTENT IS READY TO BE LOADED THE CONTAINER AND IT'S CONFIGURATION SETTINGS
         * ARE ALREADY PREPARED.
         */
    }

}

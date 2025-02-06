package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;

import java.io.Serializable;

public class Database extends BasicContentContainer implements Serializable {

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

    public Database(ContentManager contentManager) {
        super(contentManager);
    }

}

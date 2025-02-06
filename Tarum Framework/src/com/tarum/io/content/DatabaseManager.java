package com.tarum.io.content;

import com.tarum.io.content.type.ContentMap;
import com.tarum.io.content.type.Database;

public class DatabaseManager {

    private ContentManager contentManager;
    private ContentMap databaseMap;

    public DatabaseManager (){
        this (null);
    }
    public DatabaseManager (String rootDir){
        this.contentManager = new ContentManager(rootDir);

        initialize();
    }

    private void initialize(){

        /**
         * Prepare our ContentMap instance to handle database information
         * appropriately
         */
        databaseMap = new ContentMap();
        databaseMap.setKeyType(Database.class);

        
    }

}

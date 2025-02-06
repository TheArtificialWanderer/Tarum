package com.tarum.io.content.type;

import java.io.File;
import java.io.Serializable;

public class Profile extends ContentContainer implements Serializable {

    private ContentMap variableMap;

    public Profile (File file){
        super(file);
    }

}

package com.tarum.io;
import com.tarum.io.content.type.ContentMap;

import java.io.File;
import java.io.Serializable;

public class Configuration extends ContentMap implements Serializable {

    public Configuration(File file){
        super (file);
    }

}

package com.tarum.io;
import com.tarum.io.content.type.ContentMap;

import java.io.File;
import java.io.Serializable;

public class Config extends ContentMap implements Serializable {

    public Config (File file){
        super (file);
    }

}

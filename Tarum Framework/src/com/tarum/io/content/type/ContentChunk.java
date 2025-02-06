package com.tarum.io.content.type;

import java.io.Serializable;

public class ContentChunk implements Serializable {

    private long uid;
    private int index;
    private long versionToken;

    private int contentLength;
    private byte[] data;



}

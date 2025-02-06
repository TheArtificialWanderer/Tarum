package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public class ContentContainerImpl extends ContentContainer implements Serializable {

    public ContentContainerImpl(){
        this(new ContentManager());
    }
    public ContentContainerImpl(ContentManager contentManager) {
        super(contentManager);
    }
    public ContentContainerImpl(ContentManager contentManager, File file) {
        super(contentManager, file);
    }
    public ContentContainerImpl(File file) {
        super(file);
    }
    public ContentContainerImpl(ContentFormat contentFormat, File file) {
        super(contentFormat, file);
    }
    public ContentContainerImpl(ContentManager contentManager, ContentFormat contentFormat, File file) {
        super(contentManager, contentFormat, file);
    }

}

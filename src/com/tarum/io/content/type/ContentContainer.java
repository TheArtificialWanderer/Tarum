package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.util.GlueList;

import java.io.File;
import java.io.Serializable;

public abstract class ContentContainer extends BasicContentContainer implements Serializable {

    private ContentFormat contentFormat;

    private GlueList<ContentField> fieldList = new GlueList<>();

    public ContentFormat getContentFormat() {
        return contentFormat;
    }
    public void setContentFormat(ContentFormat contentFormat) {
        this.contentFormat = contentFormat;
    }

    public ContentContainer (){
        this(new ContentManager());
    }
    public ContentContainer(ContentManager contentManager) {
        super(contentManager);
    }
    public ContentContainer(ContentManager contentManager, File file) {
        super(contentManager, file);
    }
    public ContentContainer(File file) {
        super(file);
    }
    public ContentContainer(ContentFormat contentFormat, File file) {
        super(file);
        this.contentFormat = contentFormat;
    }
    public ContentContainer(ContentManager contentManager, ContentFormat contentFormat, File file) {
        super(contentManager, file);
        this.contentFormat = contentFormat;
    }

    public ContentField createField (String name){
        ContentField contentField = new ContentField(this, name);
        fieldList.add(contentField);
        return contentField;
    }

}

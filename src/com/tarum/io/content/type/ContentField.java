package com.tarum.io.content.type;
import org.w3c.dom.*;

import java.io.Serializable;

public class ContentField extends ContentNode implements Serializable {

    private FieldType fieldType = FieldType.Primitive;

    public static enum FieldType{
        Primitive, Object, Primitive_Array, Object_Array, List, GlueList, Map, HashMap
    }

    public FieldType getFieldType() {
        return fieldType;
    }
    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public ContentField(ContentContainer container) {
        super(container);
    }
    public ContentField(ContentContainer container, String name) {
        super(container, name);
    }
    public ContentField(ContentContainer container, String name, long uid) {
        super(container, name, uid);
    }



}

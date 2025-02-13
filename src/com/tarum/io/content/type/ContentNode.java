package com.tarum.io.content.type;
import com.tarum.util.MathUtils;

import java.io.Serializable;

public abstract class ContentNode implements Serializable {

    private transient ContentContainer container;
    private long uid;
    private String name;
    private Object value;

    public ContentContainer getContainer() {
        return container;
    }
    public void setContainer(ContentContainer container) {
        this.container = container;
    }
    public long getUID() {
        return uid;
    }
    public void setUID(long uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ContentNode(ContentContainer container){
        this(container, null);
    }
    public ContentNode(ContentContainer container, String name){
        this(container, name, 0);
    }
    public ContentNode(ContentContainer container, String name, long uid){
        this.container = container == null ? new ContentContainerImpl() : container;
        setName(name == null ? "Content_Node" : name);
        if (container instanceof XMLDocument){
            XMLDocument doc = (XMLDocument) container;
            // TODO: ****
        }

        this.uid = uid <= 0 ? MathUtils.GenerateUID() : uid;
    }

    public void onValueChanged (Object previousValue, Object newValue){

    }
}

package com.tarum.net;

import com.tarum.util.MathUtils;

import java.io.Serializable;

public class Packet implements Serializable {

    private long uid;
    private int id;
    private long associatedClientUID;
    private byte[] data;
    private boolean outgoing = true;

    private long creationTime, enqueueTime;

    public long getUID() {
        return uid;
    }
    public void setUID(long uid) {
        this.uid = uid;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getAssociatedClientUID() {
        return associatedClientUID;
    }
    public void setAssociatedClientUID(long associatedClientUID) {
        this.associatedClientUID = associatedClientUID;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public boolean isOutgoing() {
        return outgoing;
    }
    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public Packet (){
        this(0, null);
    }
    public Packet (int id, byte[] data){
        this(id, 0, data);
    }
    public Packet (int id, long associatedClientUID, byte[] data){
        this.id = id;
        this.associatedClientUID = associatedClientUID;
        this.data = data;
        this.uid = MathUtils.GenerateUID();
    }



}

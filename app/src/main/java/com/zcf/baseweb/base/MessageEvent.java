package com.zcf.baseweb.base;

public class MessageEvent {

    private Boolean isConn;


    public MessageEvent(Boolean isConn) {
        this.isConn = isConn;
    }

    public Boolean getConn() {
        return isConn;
    }

    public void setConn(Boolean conn) {
        isConn = conn;
    }
}

package com.example.andriodlabs;

public class Message {

    private String id;
    private String message;
    private boolean isSend;

    public Message (boolean isSend, String message){
        this.isSend = isSend;
        this.message = message;
    }

    public Message (boolean isSend, String message, String id){
        this.isSend = isSend;
        this.message = message;
        this.id = id;
    }

    public boolean isSend() {
        return isSend;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

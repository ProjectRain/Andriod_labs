package com.example.andriodlabs;

public class Message {

    private String message;
    private boolean isSend;

    public Message (boolean isSend, String message){
        this.isSend = isSend;
        this.message = message;

    }

    public boolean isSend() {
        return isSend;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.andriodlabs;

public class Chat {


    String imageId;
    String message;
    boolean isSend;

    public Chat(String imageId, String message, boolean isSend) {
        this.imageId = imageId;
        this.message = message;
        this.isSend = isSend;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}

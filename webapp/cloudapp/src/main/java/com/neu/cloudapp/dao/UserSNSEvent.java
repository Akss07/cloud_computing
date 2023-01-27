package com.neu.cloudapp.dao;

public class UserSNSEvent {
    private String email;
    private String token;
    private String eventType;

    public UserSNSEvent() {
    }

    public UserSNSEvent(String email, String token, String eventType) {
        this.email = email;
        this.token =token;
        this.eventType = eventType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}

package com.example.mimir.entities;


import com.example.mimir.dto.SessionData;

public class Session {
    private String sessionId;
    private SessionData sessionData;

    // Getters and Setters
    public String getUserId() {
        return sessionId;
    }

    public void setUserId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }
}

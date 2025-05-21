package com.example.safetyapp;

public class AlertModel {
    public String message;
    public long timestamp;

    public AlertModel() {} // Needed for Firestore

    public AlertModel(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

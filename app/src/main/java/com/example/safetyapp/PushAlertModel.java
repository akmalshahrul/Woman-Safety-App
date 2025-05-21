package com.example.safetyapp;

public class PushAlertModel {
    public String message;
    public long timestamp;

    public PushAlertModel() {}  // Required for Firestore

    public PushAlertModel(String message, long timestamp) {
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

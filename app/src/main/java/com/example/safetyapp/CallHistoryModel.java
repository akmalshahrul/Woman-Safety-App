package com.example.safetyapp;

public class CallHistoryModel {
    public String name;
    public String phone;
    public long timestamp;

    public CallHistoryModel() {}

    public CallHistoryModel(String name, String phone, long timestamp) {
        this.name = name;
        this.phone = phone;
        this.timestamp = timestamp;
    }
}

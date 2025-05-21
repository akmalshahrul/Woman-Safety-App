package com.example.safetyapp;

public class LocationAlertModel {
    public double latitude;
    public double longitude;
    public long timestamp;

    public LocationAlertModel() {}

    public LocationAlertModel(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }
}

package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;

    private Button btnSos;
    private TextView navHome, navLocation, navCall, navAlert, navProfile;

    // Sample red zone
    private static final LatLng redZone = new LatLng(3.0738, 101.5183);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // UI
        btnSos = findViewById(R.id.btn_sos);
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        btnSos.setOnClickListener(v -> triggerSosAlert());

        // Navigation bar
        navHome.setOnClickListener(v -> startActivity(new Intent(this, HomePageActivity.class)));
        navLocation.setOnClickListener(v -> recreate());
        navCall.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        navAlert.setOnClickListener(v -> startActivity(new Intent(this, AlertActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
    }

    private void triggerSosAlert() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        sendEmergencyAlert(lat, lon);
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmergencyAlert(double lat, double lon) {
        LatLng alertLocation = new LatLng(lat, lon);

        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(alertLocation)
                    .title("🚨 Alert Sent")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(alertLocation, 16f));
        }

        Map<String, Object> alert = new HashMap<>();
        alert.put("latitude", lat);
        alert.put("longitude", lon);
        alert.put("timestamp", System.currentTimeMillis());
        alert.put("message", "🚨 Alert: You are in a red zone or near a dangerous neighborhood.");

        db.collection("alerts").add(alert)
                .addOnSuccessListener(doc ->
                        Toast.makeText(this, "🚨 Alert sent!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
                        checkGeofence(location);
                    }
                });

        mMap.addCircle(new CircleOptions()
                .center(redZone)
                .radius(150)
                .strokeColor(Color.RED)
                .fillColor(0x44FF0000)
                .strokeWidth(4));
    }

    private void checkGeofence(Location currentLocation) {
        Location danger = new Location("");
        danger.setLatitude(redZone.latitude);
        danger.setLongitude(redZone.longitude);

        float distance = currentLocation.distanceTo(danger);
        if (distance <= 150) {
            Toast.makeText(this, "🚨 You are inside a danger zone!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) onMapReady(mMap);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

package com.csci200.sushantsusan39.pathliner;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Firebase mFirebase;
    HashMap startLoc, endLoc;
    double startLat, endLat, startLong, endLong;
    private GoogleMap mMap;
    LatLng start1, end1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFirebase = new Firebase("https://exam2001.firebaseio.com");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mFirebase.child("Start").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startLoc = (dataSnapshot.getValue(HashMap.class));
                startLat = Double.valueOf((Double) startLoc.get("latitude"));
                startLong = Double.valueOf((Double) startLoc.get("longitude"));
                start1 = new LatLng(endLat, endLong);
                mMap.addMarker(new MarkerOptions().position(start1).title("Start Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(start1));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mFirebase.child("End").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                endLoc = (dataSnapshot.getValue(HashMap.class));
                endLat = Double.valueOf((Double) endLoc.get("latitude"));
                endLong = Double.valueOf((Double) endLoc.get("longitude"));
                end1 = new LatLng(endLat, endLong);
                mMap.addMarker(new MarkerOptions().position(end1).title("End Location"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        System.out.println("Susan");
        System.out.println("bhattarau");
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(end1));
    }
}

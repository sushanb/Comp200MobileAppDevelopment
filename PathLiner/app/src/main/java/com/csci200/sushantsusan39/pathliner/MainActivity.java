package com.csci200.sushantsusan39.pathliner;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocationManager mLocationManager;
    Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        System.out.println(getLocationData().toString());
    }


    private boolean checkLocationStatus(){
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            String toastMessage = "Please turn your GPS location";
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public  Location getLocationData(){
        if (!checkLocationStatus()) {
            return null;
        } else {
            try {
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                        mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                        mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return mLocation;
            }catch (SecurityException e){
                return null;
            }
        }
    }
}

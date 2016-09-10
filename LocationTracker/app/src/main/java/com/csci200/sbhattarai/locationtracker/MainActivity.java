package com.csci200.sbhattarai.locationtracker;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    Location loc, dest;
    LocationManager locationManager;
    Button mButton, calDistanceButton;
    String mapString;
    EditText destLatEditText, destLongEditText;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mButton = (Button) findViewById(R.id.show_map);
        destLatEditText = (EditText) findViewById(R.id.num_lat);
        destLongEditText = (EditText) findViewById(R.id.num_long);
        calDistanceButton = (Button) findViewById(R.id.cal_distance);

        calDistanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcDistance();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                loc = getGPSUpdates();
                if (loc != null) {
                    mapString = String.format("geo:%f, %f", loc.getLatitude(), loc.getLongitude());
                    Uri gmmIntentUri = Uri.parse(mapString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "GPS turned off", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public void calcDistance(){
        loc = getGPSUpdates();
        dest = new Location("");
        double Lat = Double.parseDouble(destLatEditText.getText().toString());
        double Long  = Double.parseDouble(destLongEditText.getText().toString());
        dest.setLatitude(Lat);
        dest.setLongitude(Long);
        String distanceInKm = Math.round((double) loc.distanceTo(dest)/1000) + " Kilometers";
        promptDialogMessage(distanceInKm);
    }
    public Location getGPSUpdates() {
        if (!checkLocation())
            return null;
        else {
            try {
                loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return loc;
            } catch (SecurityException e) {
                return null;
            }
        }
    }

    public void promptDialogMessage(final String distance){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Distance")
                .setMessage("The distance is " + distance)
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Uri uri = Uri.parse("smsto:2024034173");
                        Intent shareIntent = new Intent(Intent.ACTION_SEND, uri);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, distance);
                        startActivity(shareIntent);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                    }
                });
        dialog.show();
    }
};
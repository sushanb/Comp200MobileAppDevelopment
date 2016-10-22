package com.csci200.sushantsusan39.exam1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FrontActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;
    private EditText mfirstNameEditText, mlastNameEditText;
    private Button mGreetings, mShowPlace, mPopularRes, mSaveMyCar, mFindMyCar;
    private SensorManager senseManage;
    private Sensor envSense;

    LocationManager locationManager;
    Location loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        Firebase.setAndroidContext(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        senseManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        int minTime = 60000;
        float minDistance = 15;
        MyLocationListener myLocListener = new MyLocationListener();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(false);
        String bestProvider = locationManager.getBestProvider(criteria, false);
        try {
            locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, myLocListener);
        } catch (SecurityException e){
            System.out.println("Susan");
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGreetings = (Button) findViewById(R.id.greetings);

        mGreetings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                grabDataAndGreetingsIntent();
            }
        });

        mShowPlace = (Button) findViewById(R.id.show_nearby);


        mShowPlace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder mIntentBuilder;
                    mIntentBuilder = new PlacePicker.IntentBuilder();
                    Intent mIntent = mIntentBuilder.build(getApplicationContext());
                    startActivityForResult(mIntent, PLACE_PICKER_REQUEST);

                }catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == PLACE_PICKER_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(data, getApplicationContext());
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mSaveMyCar = (Button) findViewById(R.id.save_my_car_location);

        mSaveMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc = getGPSUpdates();
                Firebase carLocation = new Firebase("https://exam2001.firebaseio.com");
                final Firebase allLocationData = carLocation.child("CarData");
                allLocationData.setValue(loc.getLatitude() + "," + loc.getLongitude());
                Toast toast=Toast.makeText(getApplicationContext(),"Car Location Saved",Toast.LENGTH_SHORT);
                toast.show();

            }
        });


        mFindMyCar = (Button) findViewById(R.id.find_my_car);
        mFindMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase myCarReference = new Firebase("https://exam2001.firebaseio.com");
                myCarReference.child("CarData").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      String latlong = (dataSnapshot.getValue().toString());
                      String[] latlongArray = latlong.split(",");
                      double latitude = Double.parseDouble(latlongArray[0]);
                      double longitude = Double.parseDouble(latlongArray[1]);
                        String labelLocation = "My Car Parking Location";
                        System.out.println(latitude);
                        System.out.println(longitude);
                        String urlAddress = "http://maps.google.com/maps?q="+ latitude  +"," + longitude +"("+ labelLocation + ")&iwloc=A&hl=es";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                        startActivity(intent);

                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println(firebaseError);
                    }
                });
            }
        });

        mPopularRes = (Button) findViewById(R.id.pop_rest);
        mPopularRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popRest = new Intent(FrontActivity.this, PopularRestaurant.class);
                startActivity(popRest);
            }
        });

            MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 10000, 100000);


    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            String message = "";
            Random rand = new Random();
            int temp = rand.nextInt(100 - 50 + 1) + 50;
            if (temp < 50){
                message = "It is too cold outside. Please bring a winter coat";
            } else if (temp < 60){
                message = "It is chilly outside. Remember to bring a jacket";
            } else if (temp < 70) {
                message = "The weather is clear outside but still bring a coat";
            } else if (temp < 80){
                message = "Take an umbrella with you. It's going to rain.";
            } else if (temp < 90) {
                message = "The weather is too sunny today.";
            } else if (temp < 100){
                message = "Weather is very hot today.";
            }
            generateNotification(getApplicationContext(), message);
        }
    }

    private void generateNotification(Context context, String message){
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, FrontActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Random rand = new Random();

        notification = builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.ic_plusone_small_off_client)
                        .setTicker("Own App")
                        .setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("Weather Update")
                        .setContentText(message)
                        .build();
        notificationManager.notify((int) when, notification);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            System.out.println("Haha");
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

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


    private void grabDataAndGreetingsIntent() {
        mfirstNameEditText = (EditText) findViewById(R.id.editText);
        mlastNameEditText = (EditText) findViewById(R.id.editText2);
        System.out.println(mfirstNameEditText.getText().toString());
        Bundle mBundle = new Bundle();
        mBundle.putString("firstName", mlastNameEditText.getText().toString());
        mBundle.putString("lastName", mfirstNameEditText.getText().toString());
        Firebase myFirebaseRef = new Firebase("https://exam2001.firebaseio.com");
        final Firebase notifications = myFirebaseRef.child("PeopleData");
        Map notification = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        notification.put("firstName", mlastNameEditText.getText().toString());
        notification.put("lastName", mfirstNameEditText.getText().toString());
        notification.put("timeStamp", dateFormat.format(date));
        notifications.push().setValue(notification);
        Intent greetingsIntent = new Intent(FrontActivity.this, GreetingsActivity.class);
        greetingsIntent.putExtras(mBundle);
        startActivity(greetingsIntent);
    };

    private class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            if (loc != null)
            {
                Firebase myFirebaseRef = new Firebase("https://exam2001.firebaseio.com");
                final Firebase locationsData = myFirebaseRef.child("LocationData");
                Map latlong = new HashMap<>();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                loc = null;
                String noFound = "Not Found";
                try {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }  catch (SecurityException e) {
                    noFound = "Not Found At All";
                }
                if (loc != null){
                latlong.put("latitude", loc.getLatitude());
                latlong.put("longitude", loc.getLongitude());}
                else {
                    latlong.put("latitude", null);
                    latlong.put("longitude", null);
                }
                latlong.put("timeStamp", dateFormat.format(date));
                locationsData.push().setValue(latlong);

            }
        }

        @Override
        public void onProviderDisabled(String arg0)
        {
        }

        @Override
        public void onProviderEnabled(String arg0)
        {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
        {
        }
    }



}

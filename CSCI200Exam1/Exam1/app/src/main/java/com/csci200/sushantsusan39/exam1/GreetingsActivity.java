package com.csci200.sushantsusan39.exam1;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;


import java.util.Locale;

public class GreetingsActivity extends AppCompatActivity {
    private TextView fname, lname, mgreetingString, mainGreetings;
    private  TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        Bundle namesBundle = getIntent().getExtras();
        String firstName = "";
        String lastName = "";
        if (namesBundle != null){
            firstName = namesBundle.getString("firstName");
            lastName = namesBundle.getString("lastName");
        }
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
        tts.speak("Susan", TextToSpeech.QUEUE_FLUSH, null, null);
        showAndPlayGreetings(firstName, lastName);
    }

    private void showAndPlayGreetings(String firstName, String lastName){
       Time now = new Time();
        String greetingsNow = "";
        now.setToNow();
        if (now.hour > 12) {
            greetingsNow = "Good Afternoon";
        } else if (now.hour > 18){
            greetingsNow = "Good Evening";
        } else if (now.hour > 22){
            greetingsNow = "Good Night";
        } else {
            greetingsNow = "Good Morning";
        };
        String hourNow = "";
        if (now.hour > 12) {
            hourNow = "It is " + (now.hour - 12) + " PM now.";
        } else {
            hourNow = "It is " + (now.hour) + " AM now.";
        }

        fname = (TextView) findViewById(R.id.fname);
        fname.setText(lastName);
        lname = (TextView)  findViewById(R.id.lname);
        lname.setText(firstName);
        mgreetingString = (TextView) findViewById(R.id.timestring);
        mgreetingString.setText(hourNow);
        mainGreetings = (TextView) findViewById(R.id.greety);
        mainGreetings.setText(greetingsNow);
        speakGreetings(greetingsNow + firstName + lastName);

    }

    private void speakGreetings(String allGreetings) {
        tts.speak(allGreetings, TextToSpeech.QUEUE_FLUSH, null, null);


    }
}

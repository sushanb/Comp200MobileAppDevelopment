package com.example.sbhattarai.susan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;


public class SpeakPlayer extends AppCompatActivity {
    private Button btnPlayer1, btnPlayer2, resetButton,dialogScore;
    private TextToSpeech tts;
    private int score1, score2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score1 = 0;
        score2 = 0;
        btnPlayer1 = (Button) findViewById(R.id.player1);
        btnPlayer2 = (Button) findViewById(R.id.player2);
        resetButton = (Button) findViewById(R.id.resetButton);
        dialogScore = (Button) findViewById(R.id.dialogscore);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
        btnPlayer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                tts.speak("Player 1", TextToSpeech.QUEUE_FLUSH, null, null);
                score1 += 1;
                System.out.println(score1);
            };
        });

        btnPlayer2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                tts.speak("Player 2", TextToSpeech.QUEUE_FLUSH, null, null);
                score2 += 1;
                System.out.println(score2);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
               score1 = 0;
               score2 = 0;
            }
        });

        dialogScore.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AlertDialog alertScore = new AlertDialog.Builder(SpeakPlayer.this).create();
                if (score1 > score2) {
                    alertScore.setTitle("Player 1 wins");
                } else if (score2 > score1) {
                    alertScore.setTitle("Player 2 wins.");
                } else if (score1 == 0 && score2 == 0){
                    alertScore.setTitle("Voting has not started yet.");
                } else {
                    alertScore.setTitle("The score is level");
                }
                if (score1 > 0 || score2 > 0) {
                    double player1Ratio = (double) score1 * 100 / (score1 + score2);
                    double player2Ratio = (double) score2 * 100 / (score1 + score2);
                    String result = "Percentage vote for Player 1 is " + player1Ratio + "%. Percentage vote for Player 2 is " + player2Ratio + "%";
                    alertScore.setMessage(result);
                    alertScore.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });

                } else {
                    alertScore.setMessage("Start voting");
                    alertScore.setButton(DialogInterface.BUTTON_NEGATIVE, "Vote", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });
                }
                alertScore.show();
            }
        });


    }
    @Override
    protected void onPause(){
        super.onPause();
        score1 = 0;
        score2 = 0;
    }

}

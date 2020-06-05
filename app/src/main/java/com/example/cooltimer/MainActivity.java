package com.example.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private SeekBar seekBar;
    private boolean isTimerOn;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.timerTextView);
        button = findViewById(R.id.startButton);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(600);
        seekBar.setProgress(30);
        isTimerOn = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer((long)progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void start(View view) {

        if(!isTimerOn){
            button.setText("Stop!");
            seekBar.setEnabled(false);  // user cannot changed seekBar
            isTimerOn = true;
            countDownTimer = new CountDownTimer(seekBar.getProgress()*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell1);
                    mediaPlayer.start();
                    resetTimer();
                }
            }; //.start(); // for start
            countDownTimer.start();
        }else{
            resetTimer();
        }

    }

    private void updateTimer(long millisUntilFinished){
        int minutes = (int)millisUntilFinished/ 1000 / 60; // to minutes
        int seconds = (int)millisUntilFinished/ 1000 - (minutes * 60);

        String minutesString = "";
        String secondsString = "";

        if (minutes < 10){
            minutesString = "0" + minutes;
        }else{
            minutesString = String.valueOf(minutes);
        }
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = String.valueOf(seconds);
        }
        textView.setText(minutesString + ":" + secondsString);
    }

    private void resetTimer(){
        countDownTimer.cancel();
        textView.setText("00:30");
        button.setText("Start!");
        seekBar.setEnabled(true);
        seekBar.setProgress(30);
        isTimerOn = false;
    }
}

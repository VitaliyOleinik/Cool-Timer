package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private TextView textView;
    private Button button;
    private SeekBar seekBar;
    private boolean isTimerOn;
    private CountDownTimer countDownTimer;
    private int defaultInterval;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        textView = findViewById(R.id.timerTextView);
        button = findViewById(R.id.startButton);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(3600);
        setIntervalFromSharedPreferences(sharedPreferences);
        isTimerOn = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long progressInMillis = progress * 1000;
                updateTimer(progressInMillis);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void start(View view) {

        if (!isTimerOn) {
            button.setText("Stop!");
            seekBar.setEnabled(false);  // user cannot changed seekBar
            isTimerOn = true;
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer(millisUntilFinished);
                }

                @Override
                public void onFinish() {

                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    // checking for checkBox
                    if(sharedPreferences.getBoolean("enable_sound", true)){

                        String melodyName = sharedPreferences.getString("timer_melody", "bell");
                        if(melodyName.equals("bell")){
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell2);
                            mediaPlayer.start();
                        } else if(melodyName.equals("alarm")){
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell1);
                            mediaPlayer.start();
                        } else if(melodyName.equals("bip")){
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell3);
                            mediaPlayer.start();
                        }



                    }
                    resetTimer();
                }
            }; //.start(); // for start
            countDownTimer.start();
        } else {
            resetTimer();
        }

    }

    private void updateTimer(long millisUntilFinished) {
        int minutes = (int) millisUntilFinished / 1000 / 60; // to minutes
        int seconds = (int) millisUntilFinished / 1000 - (minutes * 60);

        String minutesString = "";
        String secondsString = "";

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = String.valueOf(minutes);
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = String.valueOf(seconds);
        }
        textView.setText(minutesString + ":" + secondsString);
    }

    private void resetTimer() {
        countDownTimer.cancel();
        button.setText("Start!");
        seekBar.setEnabled(true);
        isTimerOn = false;
        setIntervalFromSharedPreferences(sharedPreferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        } else if (id == R.id.action_about) {
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        } else if (id == R.id.action_purchase) {
            Intent openPurchase = new Intent(this, PurchaseActivity.class);
            startActivity(openPurchase);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void setIntervalFromSharedPreferences(SharedPreferences sharedPreferences){
        defaultInterval = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("default_interval", "30")));
        long defaultIntervalInMillis = defaultInterval * 1000;
        updateTimer(defaultIntervalInMillis);
        seekBar.setProgress(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("default_interval")){
            setIntervalFromSharedPreferences(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private EditText hourInput, minuteInput, secondInput;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerDisplay = findViewById(R.id.timerDisplay);
        hourInput = findViewById(R.id.hourInput);
        minuteInput = findViewById(R.id.minuteInput);
        secondInput = findViewById(R.id.secondInput);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (isRunning) {
            return;  // Don't start if already running
        }

        // Validate user input for hours, minutes, and seconds
        int hours = parseInput(hourInput.getText().toString());
        int minutes = parseInput(minuteInput.getText().toString());
        int seconds = parseInput(secondInput.getText().toString());

        if (hours == 0 && minutes == 0 && seconds == 0) {
            Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate time in milliseconds
        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                playSound();
                Toast.makeText(TimerActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        isRunning = true;
    }

    private int parseInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0; // Return 0 if input is not valid
        }
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        timeLeftInMillis = 0;
        updateTimerDisplay();
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(time);
    }

    private void playSound() {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound1); // Ensure sound1 exists in res/raw folder
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace(); // Handle if the sound is not found
        }
    }
}

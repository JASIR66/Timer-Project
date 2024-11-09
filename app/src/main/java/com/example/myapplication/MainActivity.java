package com.example.myapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private EditText hourInput, minuteInput, secondInput;
    private Button startButton, pauseButton, resetButton;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 0;
    private long timeAtPause = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Initially, disable the pause button
        pauseButton.setEnabled(false);
        pauseButton.setVisibility(View.INVISIBLE);  // Hide the pause button initially
    }

    private void startTimer() {
        // Input validation
        String hourText = hourInput.getText().toString();
        String minuteText = minuteInput.getText().toString();
        String secondText = secondInput.getText().toString();

        if (hourText.isEmpty() || minuteText.isEmpty() || secondText.isEmpty()) {
            Toast.makeText(this, "Please enter values for hours, minutes, and seconds.", Toast.LENGTH_SHORT).show();
            return;
        }

        long hours = 0, minutes = 0, seconds = 0;
        try {
            hours = Long.parseLong(hourText);
            minutes = Long.parseLong(minuteText);
            seconds = Long.parseLong(secondText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate time in milliseconds
        timeLeftInMillis = (hours * 3600000) + (minutes * 60000) + (seconds * 1000);

        if (timeLeftInMillis <= 0) {
            Toast.makeText(this, "Please enter a valid time greater than 0.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Start the countdown timer
        if (timerRunning) {
            // If the timer is already running, cancel it and reset
            countDownTimer.cancel();
            timerRunning = false;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerDisplay.setText("00:00:00");
                timerRunning = false;
                startButton.setEnabled(true);  // Re-enable Start button after finish
                pauseButton.setEnabled(false); // Disable Pause button after timer finishes
                pauseButton.setVisibility(View.INVISIBLE);  // Hide Pause button when finished
            }
        }.start();

        timerRunning = true;
        startButton.setEnabled(false);  // Disable Start button while timer is running
        pauseButton.setEnabled(true);  // Enable Pause button while timer is running
        pauseButton.setVisibility(View.VISIBLE);  // Make sure it's visible when the timer starts
    }

    private void pauseTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timeAtPause = timeLeftInMillis;  // Store the current time left
            timerRunning = false;
            startButton.setEnabled(true);  // Re-enable Start button when paused
            pauseButton.setEnabled(false); // Disable Pause button when timer is paused
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerDisplay.setText("00:00:00");
        timeLeftInMillis = 0;
        timeAtPause = 0;
        timerRunning = false;
        startButton.setEnabled(true);  // Enable Start button when reset
        pauseButton.setEnabled(false); // Disable Pause button when reset
        pauseButton.setVisibility(View.INVISIBLE);  // Optionally hide the pause button when reset

        // Optionally clear the inputs
        hourInput.setText("");
        minuteInput.setText("");
        secondInput.setText("");
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 3600000);
        int minutes = (int) ((timeLeftInMillis % 3600000) / 60000);
        int seconds = (int) ((timeLeftInMillis % 60000) / 1000);

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }
}

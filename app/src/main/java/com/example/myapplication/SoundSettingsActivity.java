package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        Button sound1 = findViewById(R.id.sound1Button);
        Button sound2 = findViewById(R.id.sound2Button);
        Button sound3 = findViewById(R.id.sound3Button);

        sound1.setOnClickListener(v -> playSound(R.raw.sound1));
        sound2.setOnClickListener(v -> playSound(R.raw.sound2));
        sound3.setOnClickListener(v -> playSound(R.raw.sound3));
    }

    private void playSound(int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}

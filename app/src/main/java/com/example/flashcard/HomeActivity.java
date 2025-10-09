package com.example.flashcard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Musique
        AudioKit.startBgm(this, R.raw.theme_sound, true);
        AudioKit.setBgmVolume(0.2f, 0.2f);

        // SFX préchargés (optionnel)
        AudioKit.preloadSfx(this, R.raw.hmmm_sound);
        AudioKit.preloadSfx(this, R.raw.marge_toi_alors_sound);
        AudioKit.preloadSfx(this, R.raw.ohpinaise_sound);
        AudioKit.preloadSfx(this, R.raw.pas_baratin_sound);
        AudioKit.preloadSfx(this, R.raw.ta_gueule_sound);
        AudioKit.preloadSfx(this, R.raw.woohoo_sound);

        ImageButton playButton = findViewById(R.id.playButton);
        ImageButton aboutButton = findViewById(R.id.aboutButton);
        ImageButton questionButton = findViewById(R.id.questionButton);

        // Hover + touch scale pour about/question
        setupHoverScale(aboutButton, 1.2f);
        setupHoverScale(questionButton, 1.2f);

        // About
        aboutButton.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AboutActivity.class)));

        // Go écran difficulté
        playButton.setOnClickListener(v ->
                startActivity(new Intent(this, DifficultChoiceActivity.class)));

    }


    @Override
    protected void onPause() {
        super.onPause();
        AudioKit.pauseBgm();
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setupHoverScale(ImageButton btn, float scaleUp) {
        final float baseX = btn.getScaleX();
        final float baseY = btn.getScaleY();

        Runnable up = () -> btn.animate().scaleX(baseX * scaleUp).scaleY(baseY * scaleUp).setDuration(120).start();
        Runnable down = () -> btn.animate().scaleX(baseX).scaleY(baseY).setDuration(120).start();

        btn.setOnHoverListener((v, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_HOVER_ENTER: up.run(); return true;
                case MotionEvent.ACTION_HOVER_EXIT:  down.run(); return true;
            }
            return false;
        });

        btn.setOnTouchListener((v, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:  up.run(); break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: down.run(); break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AudioKit.resumeBgm();
    }
}

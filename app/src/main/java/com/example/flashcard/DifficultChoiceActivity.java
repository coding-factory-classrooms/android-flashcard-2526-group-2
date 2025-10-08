package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultChoiceActivity extends AppCompatActivity {

    private String difficulty;
    private ImageButton easyImageButton, mediumImageButton, difficileImageButton, hardcoreImageButton, impossibleImageButton, validateImageButton;

    private ImageButton holdSelectedButton;
    private float scaleEasyButtonX, scaleEasyButtonY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficult_choice);

        easyImageButton       = findViewById(R.id.easyImageButton);
        mediumImageButton     = findViewById(R.id.mediumImageButton);
        difficileImageButton  = findViewById(R.id.difficileImageButton);
        hardcoreImageButton   = findViewById(R.id.hardcoreImageButton);
        impossibleImageButton = findViewById(R.id.impossibleImageButton);
        validateImageButton   = findViewById(R.id.validateImageButton);

        scaleEasyButtonX = easyImageButton.getScaleX();
        scaleEasyButtonY = easyImageButton.getScaleY();

        easyImageButton.setOnClickListener(v -> { selectDifficulty("facile");    animeScaleButton(easyImageButton); });
        mediumImageButton.setOnClickListener(v -> { selectDifficulty("moyen");   animeScaleButton(mediumImageButton); });
        difficileImageButton.setOnClickListener(v -> { selectDifficulty("difficile"); animeScaleButton(difficileImageButton); });
        hardcoreImageButton.setOnClickListener(v -> { selectDifficulty("hardcore");   animeScaleButton(hardcoreImageButton); });
        impossibleImageButton.setOnClickListener(v -> { selectDifficulty("impossible"); animeScaleButton(impossibleImageButton); });

        validateImageButton.setOnClickListener(v -> {
            if (difficulty == null) {
                Toast.makeText(this, "Choisis une difficulté.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
        });
    }

    private void selectDifficulty(String level) {
        difficulty = level;
    }

    private void animeScaleButton(ImageButton button) {
        if (holdSelectedButton == button) return;

        if (holdSelectedButton != null) {
            holdSelectedButton.setScaleX(scaleEasyButtonX);
            holdSelectedButton.setScaleY(scaleEasyButtonY);
        }

        button.setScaleX(scaleEasyButtonX * 1.2f);
        button.setScaleY(scaleEasyButtonY * 1.2f);

        holdSelectedButton = button;
    }
}
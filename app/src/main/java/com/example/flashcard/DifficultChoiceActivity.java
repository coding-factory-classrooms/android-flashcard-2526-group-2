package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultChoiceActivity extends AppCompatActivity {

    private String difficulty;
    private ImageButton easyImageButton, mediumImageButton, impossibleImageButton, validateImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficult_choice);

        easyImageButton = findViewById(R.id.easyImageButton);
        mediumImageButton = findViewById(R.id.mediumImageButton);
        impossibleImageButton = findViewById(R.id.impossibleImageButton);
        validateImageButton = findViewById(R.id.validateImageButton);
        validateImageButton.setEnabled(false);

        easyImageButton.setOnClickListener(v -> selectDifficulty("facile"));
        mediumImageButton.setOnClickListener(v -> selectDifficulty("moyen"));
        impossibleImageButton.setOnClickListener(v -> selectDifficulty("impossible"));

        // Validation
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
        validateImageButton.setEnabled(true);
    }
}
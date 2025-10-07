package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultChoiceActivity extends AppCompatActivity {

    private String difficulty;
    private Button easyButton, mediumButton, impossibleButton, validateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficult_choice);

        easyButton = findViewById(R.id.easyButton);
        mediumButton = findViewById(R.id.mediumButton);
        impossibleButton = findViewById(R.id.impossibleButton);
        validateButton = findViewById(R.id.validateButton);
        validateButton.setEnabled(false);

        easyButton.setOnClickListener(v -> selectDifficulty("facile"));
        mediumButton.setOnClickListener(v -> selectDifficulty("moyen"));
        impossibleButton.setOnClickListener(v -> selectDifficulty("impossible"));

        // Validation
        validateButton.setOnClickListener(v -> {
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
        validateButton.setEnabled(true);
    }
}
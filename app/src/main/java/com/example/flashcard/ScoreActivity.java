package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link with the ids
        TextView DifficultyText = findViewById(R.id.DifficultyText);
        TextView TextScore = findViewById(R.id.ScoreTextView);
        TextView PercentageText = findViewById(R.id.PercentageText);

        String difficulty = "";
        int correctAnswers = 0;
        int totalQuestions = 0;
        float percentage = (float) correctAnswers / totalQuestions * 100f;

        DifficultyText.setText(difficulty);
        TextScore.setText(correctAnswers / totalQuestions);
        PercentageText.setText(percentage + "");


        Button homeButton= findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        });


    }
}
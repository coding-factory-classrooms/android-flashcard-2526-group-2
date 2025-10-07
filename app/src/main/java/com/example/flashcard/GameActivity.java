package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private TextView difficultyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        difficultyTextView = findViewById(R.id.difficultyTextView);

        Intent intent = getIntent();
        String currentDifficulty = intent.getStringExtra("difficulty");

        difficultyTextView.setText(currentDifficulty != null ? currentDifficulty : "Aucune difficulté");
    }
}

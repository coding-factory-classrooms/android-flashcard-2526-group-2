package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.flashcard.AudioKit;

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
        TextView TextScore = findViewById(R.id.TextScore);
        TextView PercentageText = findViewById(R.id.PercentageText);

        // Link with GameActivity
        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");
        int goodAnswers = intent.getIntExtra("goodAnswers", 0);

        int totalQuestions;
        if (difficulty != null) {
            totalQuestions = Question.getQuestions(difficulty).size();
        } else {
            totalQuestions = 0;
        }
        int percentageInt;
        if (totalQuestions > 0) {
            percentageInt = Math.round(((float) goodAnswers / totalQuestions) * 100f);
        } else {
            percentageInt = 0;
        }

        DifficultyText.setText(difficulty);
        TextScore.setText( goodAnswers + "/" + totalQuestions);
        PercentageText.setText(percentageInt + "%");

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent HomeIntent = new Intent(this,HomeActivity.class);
            startActivity(HomeIntent);
        });


        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(view -> {

            String shareText = "J'ai eu " + goodAnswers + "/" + totalQuestions +
                    " avec un score de : " + percentageInt + " % " + "en "+ difficulty ;

            // action share
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            //  share menu
            startActivity(Intent.createChooser(shareIntent, "Partager via"));
        });

    }
}
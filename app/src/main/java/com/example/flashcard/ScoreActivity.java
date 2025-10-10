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

        // Lien avec les xml
        TextView DifficultyText = findViewById(R.id.DifficultyText);
        TextView TextScore = findViewById(R.id.TextScore);
        TextView PercentageText = findViewById(R.id.PercentageText);

        // Recup les données de GameActivity
        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");
        int goodAnswers = intent.getIntExtra("goodAnswers", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);

        // Calcul le pourcentage de réussite
        int percentageInt;
        if (totalQuestions > 0) {
            // Arrondit le nombre
            percentageInt = Math.round(((float) goodAnswers / totalQuestions) * 100f);
        } else {
            percentageInt = 0;
        }

        // Affichage dans le xml les infos
        DifficultyText.setText(difficulty);
        TextScore.setText( goodAnswers + "/" + totalQuestions);
        PercentageText.setText(percentageInt + "%");

        // Bouton retour à l'acceuil
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            Intent HomeIntent = new Intent(this,HomeActivity.class);
            startActivity(HomeIntent);
        });


        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(view -> {
            String shareText = "J'ai eu " + goodAnswers + "/" + totalQuestions +
                    " avec un score de : " + percentageInt + " % " + "en "+ difficulty ;

            // Crée une intent de partage
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            //  menu de partage
            startActivity(Intent.createChooser(shareIntent, "Partager via"));
        });

        AudioKit.startBgm(this, R.raw.score_theme, true);

    }
}
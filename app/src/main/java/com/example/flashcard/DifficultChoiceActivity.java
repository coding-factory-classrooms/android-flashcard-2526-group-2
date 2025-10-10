package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.flashcard.HomeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultChoiceActivity extends AppCompatActivity {

    private String difficulty;
    private ImageButton easyImageButton, mediumImageButton, difficileImageButton, hardcoreImageButton, impossibleImageButton, validateImageButton;

    private ImageButton holdSelectedButton;
    private float scaleEasyButtonX, scaleEasyButtonY;
    public ImageButton titleImageButton;

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

        AudioKit.startBgm(this, R.raw.alien_sound_theme, true);

        titleImageButton= findViewById(R.id.titleImageButton);
        titleImageButton.setOnClickListener(v -> AudioKit.playLongOnce(this, R.raw.donuts_sucre_au_sucre_sound));


        scaleEasyButtonX = easyImageButton.getScaleX();
        scaleEasyButtonY = easyImageButton.getScaleY();

        easyImageButton.setOnClickListener(v -> { selectDifficulty("facile");    animeScaleOnclickButton(easyImageButton);AudioKit.playSfx(this, R.raw.c_nul_homer);});
        mediumImageButton.setOnClickListener(v -> { selectDifficulty("moyen");   animeScaleOnclickButton(mediumImageButton);AudioKit.playSfx(this, R.raw.homer_haha);});
        difficileImageButton.setOnClickListener(v -> { selectDifficulty("difficile"); animeScaleOnclickButton(difficileImageButton);AudioKit.playSfx(this, R.raw.hmmm_sound);});
        hardcoreImageButton.setOnClickListener(v -> { selectDifficulty("hardcore");   animeScaleOnclickButton(hardcoreImageButton);AudioKit.playLongOnce(this, R.raw.homer_hou); });
        impossibleImageButton.setOnClickListener(v -> { selectDifficulty("impossible"); animeScaleOnclickButton(impossibleImageButton); AudioKit.playSfx(this, R.raw.pas_baratin_sound); });


        validateImageButton.setOnClickListener(v -> {
            if (difficulty == null) {
                Toast.makeText(this, "Choisis une difficulté.", Toast.LENGTH_SHORT).show();
                return;
            }
            animeScaleOnclickButton(easyImageButton);AudioKit.playSfx(this, R.raw.croc_sound);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
        });


        HomeActivity.setupHoverScale(validateImageButton, 1.2f);
    }

    private void selectDifficulty(String level) {
        difficulty = level;
    }

    private void animeScaleOnclickButton(ImageButton button) {
        if (holdSelectedButton == button) return;

        if (holdSelectedButton != null) {
            holdSelectedButton.setScaleX(scaleEasyButtonX);
            holdSelectedButton.setScaleY(scaleEasyButtonY);
        }

        button.setScaleX(scaleEasyButtonX * 1.2f);
        button.setScaleY(scaleEasyButtonY * 1.2f);

        holdSelectedButton = button;
    }


    @Override
    protected void onPause() {
        super.onPause();
        AudioKit.releaseAll();}
}
package com.example.flashcard;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SingleQuestionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView questionView;
    private RadioGroup choicesGroup;
    private RadioButton choice1, choice2, choice3;
    private ImageButton soundButton, validateButton;

    private ImageButton soundQuestionButton1, soundQuestionButton2, soundQuestionButton3;

    private String correctAnswer;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_question);

        // Lier l'UI
        questionView = findViewById(R.id.gameTextView);
        choicesGroup = findViewById(R.id.choicesRadioGroup);
        choice1 = findViewById(R.id.gameButton1);
        choice2 = findViewById(R.id.gameButton2);
        choice3 = findViewById(R.id.gameButton3);
        soundButton = findViewById(R.id.soundImageButton);
        validateButton = findViewById(R.id.gameValidButton);

        soundQuestionButton1 = findViewById(R.id.soundQuestionButton1);
        soundQuestionButton2 = findViewById(R.id.soundQuestionButton2);
        soundQuestionButton3 = findViewById(R.id.soundQuestionButton3);

        // Lire les extras de l'Intent
        String questionText = getIntent().getStringExtra("questionText");
        String[] options = getIntent().getStringArrayExtra("options");
        correctAnswer = getIntent().getStringExtra("correctAnswer");

        if (questionText == null) questionText = "";
        questionView.setText(questionText);

        if (options != null && options.length >= 3) {
            // Mélanger les options
            List<String> shuffledOptions = new ArrayList<>();
            Collections.addAll(shuffledOptions, options);
            Collections.shuffle(shuffledOptions);

            // Assigner aux boutons
            choice1.setText(shuffledOptions.get(0));
            choice2.setText(shuffledOptions.get(1));
            choice3.setText(shuffledOptions.get(2));

            // Activer les mégaphones
            soundQuestionButton1.setEnabled(true);
            soundQuestionButton2.setEnabled(true);
            soundQuestionButton3.setEnabled(true);

            soundQuestionButton1.setOnClickListener(v -> speak(choice1.getText().toString()));
            soundQuestionButton2.setOnClickListener(v -> speak(choice2.getText().toString()));
            soundQuestionButton3.setOnClickListener(v -> speak(choice3.getText().toString()));
        } else {
            validateButton.setEnabled(false);
            soundQuestionButton1.setEnabled(false);
            soundQuestionButton2.setEnabled(false);
            soundQuestionButton3.setEnabled(false);
            Toast.makeText(this, "Données de la question manquantes", Toast.LENGTH_LONG).show();
        }

        // Initialisation TTS
        tts = new TextToSpeech(this, this);
        soundButton.setOnClickListener(v -> speak(questionView.getText().toString()));

        // Activer valider seulement si un choix est sélectionné
        validateButton.setEnabled(false);
        choicesGroup.setOnCheckedChangeListener((group, checkedId) -> validateButton.setEnabled(checkedId != -1));

        // Vérification réponse
        validateButton.setOnClickListener(v -> {
            int checkedId = choicesGroup.getCheckedRadioButtonId();
            if (checkedId == -1) return;
            RadioButton selected = findViewById(checkedId);
            if (selected == null) return;
            checkAnswer(selected.getText().toString());
        });

        // Démarrer la musique de fond (optionnel)
        AudioKit.releaseAll();
        AudioKit.startBgm(this, R.raw.horror_theme_sound, false);
        AudioKit.setBgmVolume(0.2f, 0.2f);
    }

    private void checkAnswer(String answer) {
        boolean correct = answer.equals(correctAnswer);
        Toast.makeText(this, correct ? "Bonne réponse !" : "Mauvaise réponse...", Toast.LENGTH_SHORT).show();
        int soundRes = correct ? R.raw.woohoo_sound : R.raw.ohpinaise_sound;
        AudioKit.playLongOnce(this, soundRes);

        // Retour automatique après 500ms
        new Handler().postDelayed(() -> {
            if (tts != null) try { tts.stop(); } catch (Exception ignored) {}
            AudioKit.releaseAll();
            finish();
        }, 500);
    }

    /* ====== TTS ====== */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.FRANCE);
            tts.setPitch(1.0f);
            tts.setSpeechRate(1.0f);
            ttsReady = (res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED);
            if (!ttsReady) Toast.makeText(this, "TTS non disponible", Toast.LENGTH_SHORT).show();
        } else {
            ttsReady = false;
            Toast.makeText(this, "Initialisation TTS échouée", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String text) {
        if (!ttsReady || text == null) return;
        text = text.trim();
        if (text.isEmpty()) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioKit.releaseAll();
        if (tts != null) try { tts.stop(); } catch (Exception ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            try { tts.shutdown(); } catch (Exception ignored) {}
        }
    }
}

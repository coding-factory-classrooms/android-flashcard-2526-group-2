package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
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
import java.util.Random;
import java.util.UUID;

public class GameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // UI
    private TextView difficultyTextView, questionTextView, indexTextView, counterTextView;
    private RadioGroup choicesRadioGroup;
    private RadioButton choice1, choice2, choice3;
    private ImageButton soundImageButton, validateButton, soundQuestionButton1, soundQuestionButton2, soundQuestionButton3;

    // Alarm views
    private View alarmRedOverlay, alarmBeam;

    // Données quiz
    private List<String[]> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int goodAnswers = 0;
    private String currentDifficulty;

    // Mode “une seule question”
    private boolean singleMode = false;

    // TTS
    private TextToSpeech tts;
    private boolean ttsReady = false;

    // Timer (uniquement pour "Impossible")
    private final Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private int timeLeft = 10;
    private boolean hasAnswered = false;
    private boolean alarmActive = false;

    // Compteur de clics sur les choix (au-delà de 3 → joue un SFX)
    private int clickCountChoices = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        AudioKit.releaseAll();
        AudioKit.startBgm(this, R.raw.horror_theme_sound, false);
        AudioKit.setBgmVolume(0.2f, 0.2f);

        // TTS
        tts = new TextToSpeech(this, this);

        // Bind UI
        difficultyTextView   = findViewById(R.id.difficultyTextView);
        questionTextView     = findViewById(R.id.gameTextView);
        indexTextView        = findViewById(R.id.indexTextView);
        counterTextView      = findViewById(R.id.counterTextView);
        choicesRadioGroup    = findViewById(R.id.choicesRadioGroup);
        choice1              = findViewById(R.id.gameButton1);
        choice2              = findViewById(R.id.gameButton2);
        choice3              = findViewById(R.id.gameButton3);
        validateButton       = findViewById(R.id.gameValidButton);
        soundImageButton     = findViewById(R.id.soundImageButton);
        soundQuestionButton1 = findViewById(R.id.soundQuestionButton1);
        soundQuestionButton2 = findViewById(R.id.soundQuestionButton2);
        soundQuestionButton3 = findViewById(R.id.soundQuestionButton3);

        // Alarm views
        alarmRedOverlay = findViewById(R.id.alarmRedOverlay);
        alarmBeam       = findViewById(R.id.alarmBeam);
        alarmRedOverlay.setVisibility(View.GONE);
        alarmBeam.setVisibility(View.GONE);

        // Son boutons
        soundImageButton.setOnClickListener(v -> speak(getTextOrEmpty(questionTextView)));
        soundQuestionButton1.setOnClickListener(v -> speak(getTextOrEmpty(choice1)));
        soundQuestionButton2.setOnClickListener(v -> speak(getTextOrEmpty(choice2)));
        soundQuestionButton3.setOnClickListener(v -> speak(getTextOrEmpty(choice3)));

        // Validation
        validateButton.setEnabled(false);
        choicesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                validateButton.setEnabled(true);
                RadioButton rb = findViewById(checkedId);
                if (rb != null) selectedAnswer = rb.getText().toString();
            }
        });
        validateButton.setOnClickListener(v -> checkAnswer());

        // Clics sur les choix → compteur + son après 3 clics
        View.OnClickListener choicesClickCounter = v -> {
            clickCountChoices++;
            if (clickCountChoices > 3) {
                AudioKit.playSfx(this, R.raw.ta_gueule_sound);
            }
        };
        choice1.setOnClickListener(choicesClickCounter);
        choice2.setOnClickListener(choicesClickCounter);
        choice3.setOnClickListener(choicesClickCounter);

        // ===== Mode “une seule question” depuis la liste =====
        Intent intent = getIntent();
        singleMode = intent.getBooleanExtra("single_mode", false);
        if (singleMode) {
            String q   = intent.getStringExtra("q_text");
            String c0  = intent.getStringExtra("q_c0");
            String c1  = intent.getStringExtra("q_c1");
            String c2  = intent.getStringExtra("q_c2");
            String ans = intent.getStringExtra("q_answer");
            currentDifficulty = intent.getStringExtra("difficulty");
            if (currentDifficulty == null) currentDifficulty = "Test";

            difficultyTextView.setText(currentDifficulty);
            counterTextView.setVisibility(View.GONE); // pas de timer
            alarmRedOverlay.setVisibility(View.GONE);
            alarmBeam.setVisibility(View.GONE);

            questions = new ArrayList<>();
            questions.add(new String[]{ q, c0, c1, c2, ans });
            showQuestion(0);
            return; // ne pas charger la banque complète
        }

        // ===== Mode normal : chargement depuis JSON par difficulté =====
        currentDifficulty = intent.getStringExtra("difficulty");
        difficultyTextView.setText(currentDifficulty != null ? currentDifficulty : "Aucune difficulté");

        questions = QuestionsJSON.getQuestions(this, currentDifficulty);
        Collections.shuffle(questions);
        showQuestion(currentQuestionIndex);
    }

    /** Démarre l’alarme (visuel + son) */
    private void startAlarm() {
        if (alarmActive) return;
        alarmActive = true;
        alarmRedOverlay.setVisibility(View.VISIBLE);
        alarmBeam.setVisibility(View.VISIBLE);
        alarmRedOverlay.startAnimation(android.view.animation.AnimationUtils.loadAnimation(
                GameActivity.this, R.anim.pulse_overlay));
        alarmBeam.startAnimation(android.view.animation.AnimationUtils.loadAnimation(
                GameActivity.this, R.anim.rotate_beam));
        // son en boucle pendant l’alarme
        AudioKit.startBgm(this, R.raw.alarm_sound, true);
    }

    /** Stoppe l’alarme et cache les vues */
    private void stopAlarm() {
        if (!alarmActive) return;
        alarmActive = false;
        alarmRedOverlay.clearAnimation();
        alarmBeam.clearAnimation();
        alarmRedOverlay.setVisibility(View.GONE);
        alarmBeam.setVisibility(View.GONE);
        AudioKit.stopBgm();
    }

    // Affiche la question et (re)lance le timer uniquement pour "Impossible"
    private void showQuestion(int index) {
        if (questions == null || questions.isEmpty() || index < 0 || index >= questions.size()) return;

        String[] q = questions.get(index);
        String[] choices = { q[1], q[2], q[3] };
        List<String> shuffled = java.util.Arrays.asList(choices);
        Collections.shuffle(shuffled, new Random());

        questionTextView.setText(q[0]);
        choice1.setText(shuffled.get(0));
        choice2.setText(shuffled.get(1));
        choice3.setText(shuffled.get(2));

        indexTextView.setText("Question " + (index + 1) + " / " + questions.size());
        choicesRadioGroup.clearCheck();
        validateButton.setEnabled(false);
        selectedAnswer = "";
        hasAnswered = false;

        // reset compteur de clics sur les choix
        clickCountChoices = 0;

        // Stoppe toute alarme/timer résiduels
        stopAlarm();
        if (timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);

        // Pas de timer/alarme en mode “une seule question”
        if (singleMode) {
            counterTextView.setVisibility(View.GONE);
            return;
        }

        // === Timer + alarme UNIQUEMENT pour difficulté "Impossible" ===
        if ("Impossible".equalsIgnoreCase(currentDifficulty)) {
            counterTextView.setVisibility(View.VISIBLE);

            // réinit timer
            timeLeft = 10;
            counterTextView.setText(timeLeft + "s");

            // cacher icônes/texte après 3s (ton ancien comportement)
            int amber = android.graphics.Color.parseColor("#FFC107");
            choice1.setTextColor(amber);
            choice2.setTextColor(amber);
            choice3.setTextColor(amber);
            soundQuestionButton1.setVisibility(ImageButton.VISIBLE);
            soundQuestionButton2.setVisibility(ImageButton.VISIBLE);
            soundQuestionButton3.setVisibility(ImageButton.VISIBLE);

            choice1.postDelayed(() -> {
                choice1.setTextColor(getResources().getColor(android.R.color.transparent));
                choice2.setTextColor(getResources().getColor(android.R.color.transparent));
                choice3.setTextColor(getResources().getColor(android.R.color.transparent));
                soundQuestionButton1.setVisibility(ImageButton.INVISIBLE);
                soundQuestionButton2.setVisibility(ImageButton.INVISIBLE);
                soundQuestionButton3.setVisibility(ImageButton.INVISIBLE);
                choice1.setClickable(true);
                choice2.setClickable(true);
                choice3.setClickable(true);
            }, 3000);

            // Timer 1s
            timerRunnable = new Runnable() {
                @Override public void run() {
                    if (hasAnswered) return;

                    if (timeLeft <= 5 && !alarmActive) {
                        startAlarm();
                    }

                    if (timeLeft > 0) {
                        timeLeft--;
                        counterTextView.setText(timeLeft + "s");
                        timerHandler.postDelayed(this, 1000);
                    } else {
                        // Temps écoulé → mauvaise réponse + stop alarme
                        stopAlarm();
                        hasAnswered = true;
                        checkAnswer();
                    }
                }
            };
            timerHandler.postDelayed(timerRunnable, 1000);
        } else {
            // Pas de timer/alarme pour les autres difficultés
            counterTextView.setVisibility(View.GONE);
        }
    }

    // Vérifie la réponse et passe à la question suivante
    private void checkAnswer() {
        hasAnswered = true;
        stopAlarm();
        if (timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);

        String[] q = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(q[4])) {
            Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
            goodAnswers += 1;
            AudioKit.playLongOnce(this, R.raw.woohoo_sound);
        } else {
            Toast.makeText(this, "Mauvaise réponse ! La bonne réponse était : " + q[4], Toast.LENGTH_LONG).show();
            AudioKit.playLongOnce(this, R.raw.ohpinaise_sound);
        }

        new Handler().postDelayed(() -> {
            // En mode “une seule question”, on ferme l’activité après la réponse
            if (singleMode) {
                finish();
                return;
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("goodAnswers", goodAnswers);
                intent.putExtra("difficulty", currentDifficulty);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    /* ====== TextToSpeech ====== */
    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.FRANCE);
            tts.setSpeechRate(1.0f);
            tts.setPitch(1.0f);
            ttsReady = (res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED);
            if (!ttsReady) {
                Toast.makeText(this, "Langue TTS non disponible", Toast.LENGTH_SHORT).show();
            }
        } else {
            ttsReady = false;
            Toast.makeText(this, "Initialisation TTS échouée", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String text) {
        if (!ttsReady || text == null) return;
        String toSay = text.trim();
        if (toSay.isEmpty()) return;
        tts.speak(toSay, TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString());
    }

    private static String getTextOrEmpty(TextView tv) {
        CharSequence cs = tv.getText();
        return cs == null ? "" : cs.toString();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            try { tts.stop(); } catch (Exception ignored) {}
            try { tts.shutdown(); } catch (Exception ignored) {}
        }
    }

    @Override protected void onPause() {
        super.onPause();
        AudioKit.releaseAll();
        if (tts != null) tts.stop();
        stopAlarm();
        if (timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);
    }
}
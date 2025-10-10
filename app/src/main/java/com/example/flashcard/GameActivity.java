package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.flashcard.AudioKit;
import com.example.flashcard.HomeActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class GameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // UI
    private TextView difficultyTextView, questionTextView, indexTextView;
    private RadioGroup choicesRadioGroup;
    private Button choice1, choice2, choice3;

    // Boutons son
    private ImageButton soundImageButton, validateButton; // lit la question + valide la question
    private ImageButton soundQuestionButton1;   // lit choix 1
    private ImageButton soundQuestionButton2;   // lit choix 2
    private ImageButton soundQuestionButton3;   // lit choix 3

    // Données quiz
    private List<String[]> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int goodAnswers = 0;

    private String currentDifficulty;
    // TTS
    private TextToSpeech tts;
    private boolean ttsReady = false;

    private int answerCounter;
    private float goodAnswer = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        AudioKit.releaseAll();

        //  Start la musique de fond avec le volume qu'on veut
        AudioKit.startBgm(this, R.raw.horror_theme_sound, false); // true pour jouer en bloucle
        AudioKit.setBgmVolume(0.2f, 0.2f);



        // --- TTS natif ---
        tts = new TextToSpeech(this, this);

        // --- Liaisons XML ---
        difficultyTextView   = findViewById(R.id.difficultyTextView);
        questionTextView     = findViewById(R.id.gameTextView);
        indexTextView        = findViewById(R.id.indexTextView);
        choicesRadioGroup    = findViewById(R.id.choicesRadioGroup);
        choice1              = findViewById(R.id.gameButton1);
        choice2              = findViewById(R.id.gameButton2);
        choice3              = findViewById(R.id.gameButton3);
        validateButton       = findViewById(R.id.gameValidButton);

        soundImageButton     = findViewById(R.id.soundImageButton);
        soundQuestionButton1 = findViewById(R.id.soundQuestionButton1);
        soundQuestionButton2 = findViewById(R.id.soundQuestionButton2);
        soundQuestionButton3 = findViewById(R.id.soundQuestionButton3);

        answerCounter = 0;

        setupHoverScale(validateButton, 1.2f);

        // --- Actions boutons son (KISS) ---
        soundImageButton.setOnClickListener(v ->
                speak(getTextOrEmpty(questionTextView))
        );
        soundQuestionButton1.setOnClickListener(v ->
                speak(getTextOrEmpty(choice1))
        );
        soundQuestionButton2.setOnClickListener(v ->
                speak(getTextOrEmpty(choice2))
        );
        soundQuestionButton3.setOnClickListener(v ->
                speak(getTextOrEmpty(choice3))
        );

        // --- Logique UI de base ---
        validateButton.setEnabled(false);
        choicesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) validateButton.setEnabled(true);
        });

        Intent intent = getIntent();
        currentDifficulty = intent.getStringExtra("difficulty");
        difficultyTextView.setText(currentDifficulty != null ? currentDifficulty : "Aucune difficulté");

        // Charge et mélange les questions
       // questions = Question.getQuestions(currentDifficulty);
//        Collections.shuffle(questions);
//        showQuestion(currentQuestionIndex);

        validateButton.setOnClickListener(v -> checkAnswer());

        // Sélection de réponse (si tu utilises des Buttons pour afficher les choix)
        choice1.setOnClickListener(v ->{
            if (answerCounter >= 3f){
                AudioKit.playSfx(this, R.raw.ta_gueule_sound);
                selectedAnswer = choice1.getText().toString();
            } else {
                selectedAnswer = choice1.getText().toString();
                answerCounter += 1f;
            }
        });
        choice2.setOnClickListener(v ->{
            if (answerCounter >= 3f){
                AudioKit.playSfx(this, R.raw.ta_gueule_sound);
                selectedAnswer = choice2.getText().toString();
            } else {
                selectedAnswer = choice2.getText().toString();
                answerCounter += 1f;
            }
        });
        choice3.setOnClickListener(v ->{
            if (answerCounter >= 3f){
                AudioKit.playSfx(this, R.raw.ta_gueule_sound);
                selectedAnswer = choice3.getText().toString();
            } else {
                selectedAnswer = choice3.getText().toString();
                answerCounter += 1f;
            }
        });

        loadQuestionsFromApi();
    }

    // Charge les données de l'API
    private void loadQuestionsFromApi() {
        OkHttpClient client = new OkHttpClient();

        // requette GET
        Request request = new Request.Builder()
                .url("https://students.gryt.tech/api/L2/quizgamesimpson/")
                .build();

        // Savoir dans le log que la requette marche
        Log.i("GameActivity", "Started HTTP Request");

        // Exécution de la requette sans bloquer l'ui
        client.newCall(request).enqueue(new Callback() {
            @Override
            // message d'erreur au cas où ça marche pas
            public void onFailure(Request request, IOException e) {
                Log.e("GameActivity", "OnFailure: ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // mettre le body response HTTP en str
                String body = response.body().string();
                Log.i("GameActivity", "onResponse: body=" + body);

                try {
                    // mettre le str en json
                    JSONObject jsonObject = new JSONObject(body);
                    String difficulty = currentDifficulty;

                    // liste avec le contenu du Json
                    List<String[]> Questions = new java.util.ArrayList<>();

                    // recup le tableau qui correspond à la difficulté qu'on choisit
                    org.json.JSONArray arr = jsonObject.getJSONArray(difficulty);

                    // Parcours chaque clé question dans le tableau
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject q = arr.getJSONObject(i);
                        String question = q.getString("question");
                        // recup les options
                        org.json.JSONArray opts = q.getJSONArray("options");
                        String rep1 = opts.getString(0);
                        String rep2 = opts.getString(1);
                        String rep3 = opts.getString(2);
                        // recup la bonne réponse
                        String bonnerep = q.getString("answer");
                        // add dans la liste Question
                        Questions.add(new String[]{question, rep1, rep2, rep3, bonnerep});
                    }

                    // Mettre à jour l'interface
                    runOnUiThread(() -> {
                        questions = Questions;
                        Collections.shuffle(questions);
                        showQuestion(currentQuestionIndex);
                    });

                } catch (JSONException e) {
                    // en cas d'erreur de parsing json
                    Log.e("GameActivity", "Erreur JSON", e);
                }
            }
        });
    }

    // Affiche la question et remet l'état des choix
    private void showQuestion(int index) {
        String[] q = questions.get(index);
        String[] choices = { q[1], q[2], q[3] };

        List<String> shuffledChoices = java.util.Arrays.asList(choices);
        Collections.shuffle(shuffledChoices, new Random());

        questionTextView.setText(q[0]);
        choice1.setText(shuffledChoices.get(0));
        choice2.setText(shuffledChoices.get(1));
        choice3.setText(shuffledChoices.get(2));

        choicesRadioGroup.clearCheck();
        validateButton.setEnabled(false);
        selectedAnswer = "";
        indexTextView.setText("Question " + (index + 1) + " / " + questions.size());

        // Optionnel : lire automatiquement l’énoncé
        // speak(q[0]);
    }

    // Vérifie la réponse, toasts + passe à la suivante
    private void checkAnswer() {



        String[] q = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(q[4])) {
            Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
            goodAnswers +=1;
            AudioKit.playLongOnce(this, R.raw.woohoo_sound);
            answerCounter = 0;
            goodAnswer += 1f;
        } else {
            Toast.makeText(this, "Mauvaise réponse ! La bonne réponse était : " + q[4], Toast.LENGTH_LONG).show();
            AudioKit.playLongOnce(this, R.raw.ohpinaise_sound);
            answerCounter = 0;
        }
        choicesRadioGroup.clearCheck();

        new Handler().postDelayed(() -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("goodAnswers", goodAnswers);
                intent.putExtra("difficulty", currentDifficulty);
                intent.putExtra("Bonne réponse", goodAnswer);
                intent.putExtra("totalQuestions", questions.size());
                startActivity(intent);
                finish();
            }
        }, 500);
    }


    /* ====== TextToSpeech ====== */

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.FRANCE); // ou Locale.FRENCH
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
        String utteranceId = UUID.randomUUID().toString();
        tts.speak(toSay, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }

    private static String getTextOrEmpty(TextView tv) {
        CharSequence cs = tv.getText();
        return cs == null ? "" : cs.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            try { tts.stop(); } catch (Exception ignored) {}
            try { tts.shutdown(); } catch (Exception ignored) {}
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioKit.releaseAll();
        tts.stop();
    }

    public static void setupHoverScale(ImageButton btn, float scaleUp) {
        final float baseX = 1f, baseY = 1f;
        final long dur = 120;

        Runnable up   = () -> btn.animate().scaleX(baseX * scaleUp).scaleY(baseY * scaleUp).setDuration(dur).start();
        Runnable down = () -> btn.animate().scaleX(baseX).scaleY(baseY).setDuration(dur).start();

        // Survol (souris/stylet)
        btn.setOnHoverListener((v, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_HOVER_ENTER: up.run(); return true;
                case MotionEvent.ACTION_HOVER_EXIT:  down.run(); return true;
            }
            return false;
        });

        // Press / Release (tactile)
        btn.setOnTouchListener((v, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:  up.run(); break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: down.run(); break;
            }
            return false; // laisse le onClick
        });
    }


}

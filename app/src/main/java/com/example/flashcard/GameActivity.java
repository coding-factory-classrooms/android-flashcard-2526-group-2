package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView difficultyTextView, questionTextView;
    private RadioGroup choicesRadioGroup;
    private Button choice1, choice2, choice3, validateButton;
    private List<String[]> questions;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //liaisons XML
        difficultyTextView = findViewById(R.id.difficultyTextView);
        questionTextView = findViewById(R.id.gameTextView);
        choicesRadioGroup = findViewById(R.id.choicesRadioGroup);
        choice1 = findViewById(R.id.gameButton2);
        choice2 = findViewById(R.id.gameButton1);
        choice3 = findViewById(R.id.gameButton3);
        validateButton = findViewById(R.id.gameValidButton);

        //desactive le bouton "valider la reponse" tant qu'aucune reponse n'est choisit
        validateButton.setEnabled(false);
        choicesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                validateButton.setEnabled(true);
            }
        });

        Intent intent = getIntent();
        String currentDifficulty = intent.getStringExtra("difficulty");
        difficultyTextView.setText(currentDifficulty != null ? currentDifficulty : "Aucune difficulté");

        questions = Question.getQuestions(currentDifficulty);
        Collections.shuffle(questions);
        showQuestion(currentQuestionIndex);

        validateButton.setOnClickListener(v -> checkAnswer());

        //permet de savoir si l'utilisateru a choisi la bonne reponse ou non sinon dit que mauvaise reponse
        choice1.setOnClickListener(v -> selectedAnswer = choice1.getText().toString());
        choice2.setOnClickListener(v -> selectedAnswer = choice2.getText().toString());
        choice3.setOnClickListener(v -> selectedAnswer = choice3.getText().toString());

    }

    //permet de préparer et d'afficher la prochiane question
    private void showQuestion(int index) {
        String[] q = questions.get(index);

        String[] choices = {q[1], q[2], q[3]};
        List<String> shuffledChoices = java.util.Arrays.asList(choices);
        Collections.shuffle(shuffledChoices, new Random());

        questionTextView.setText(q[0]);
        choice1.setText(shuffledChoices.get(0));
        choice2.setText(shuffledChoices.get(1));
        choice3.setText(shuffledChoices.get(2));

        choicesRadioGroup.clearCheck();
        validateButton.setEnabled(false);
        selectedAnswer = "";
    }

    //permet de dire si il s'agit d'une bonne ou mauvaise reponse et passe à la prochiane question
    private void checkAnswer() {
        String[] q = questions.get(currentQuestionIndex);

        if (selectedAnswer.equals(q[4])) {
            Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mauvaise réponse ! La réponse était : " + q[4], Toast.LENGTH_LONG).show();
        }

        choicesRadioGroup.clearCheck();

        // ChatGPT qui donne pour mettre du délais pour les animation de point
        new android.os.Handler().postDelayed(() -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                finish();
            }
        }, 500);
    }
}

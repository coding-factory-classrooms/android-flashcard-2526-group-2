package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private ListView listViewQuestions;
    private List<String[]> allQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        listViewQuestions = findViewById(R.id.listViewQuestions);

        // Récupérer toutes les questions
        allQuestions.addAll(Question.getQuestions("facile"));
        allQuestions.addAll(Question.getQuestions("moyen"));
        allQuestions.addAll(Question.getQuestions("difficile"));

        // Liste des textes de questions
        List<String> questionTexts = new ArrayList<>();
        for (String[] q : allQuestions) {
            questionTexts.add(q[0]);
        }

        // Utilisation d'un ArrayAdapter avec ton layout personnalisé
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_question,   // layout personnalisé
                R.id.textViewQuestion,         // l'ID du TextView dans ce layout
                questionTexts
        );
        listViewQuestions.setAdapter(adapter);

        // Clic sur un item : ouvre SingleQuestionActivity
        listViewQuestions.setOnItemClickListener((parent, view, position, id) -> {
            String[] selectedQuestion = allQuestions.get(position);
            Intent intent = new Intent(this, SingleQuestionActivity.class);
            intent.putExtra("questionText", selectedQuestion[0]);
            intent.putExtra("options", new String[]{selectedQuestion[1], selectedQuestion[2], selectedQuestion[3]});
            intent.putExtra("correctAnswer", selectedQuestion[1]);
            startActivity(intent);
        });
    }
}

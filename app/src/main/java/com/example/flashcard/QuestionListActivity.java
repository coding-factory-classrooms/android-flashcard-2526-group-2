package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/** Liste toutes les questions triées par difficulté avec micro + clic pour tester une question */
public class QuestionListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private RecyclerView rv;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    private static final List<String> ORDER = Arrays.asList(
            "facile", "moyen", "difficile", "hardcore", "impossible"
    );

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        rv = findViewById(R.id.questionsRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        tts = new TextToSpeech(this, this);

        // Construire la liste aplatie (sections + questions) dans l’ordre voulu
        List<QuestionsListAdapter.Item> items = new ArrayList<>();
        for (String diff : ORDER) {
            List<String[]> qs = QuestionsJSON.getQuestions(this, diff);
            if (qs.isEmpty()) continue;
            // Section
            String title = diff.substring(0,1).toUpperCase() + diff.substring(1);
            items.add(new QuestionsListAdapter.Item.SectionItem(title));
            // Questions
            for (String[] q : qs) {
                items.add(new QuestionsListAdapter.Item.QuestionItem(diff, q));
            }
        }

        QuestionsListAdapter.Listener listener = new QuestionsListAdapter.Listener() {
            @Override public void onSpeak(String text) {
                if (ttsReady && text != null && !text.trim().isEmpty()) {
                    tts.speak(text.trim(), TextToSpeech.QUEUE_ADD, null, "qread");
                }
            }
            @Override public void onQuestionClick(QuestionsListAdapter.Item.QuestionItem item) {
                // Lancer GameActivity en MODE "UNE SEULE QUESTION"
                Intent it = new Intent(QuestionListActivity.this, GameActivity.class);
                it.putExtra("single_mode", true);
                it.putExtra("q_text",   item.q);
                it.putExtra("q_c0",     item.c0);
                it.putExtra("q_c1",     item.c1);
                it.putExtra("q_c2",     item.c2);
                it.putExtra("q_answer", item.answer);
                // (on peut aussi passer difficulty si tu veux l’afficher)
                it.putExtra("difficulty", item.difficulty);
                startActivity(it);
            }
        };

        rv.setAdapter(new QuestionsListAdapter(items, listener));
    }

    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.FRANCE);
            tts.setSpeechRate(1.0f);
            tts.setPitch(1.0f);
            ttsReady = res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            try { tts.stop(); } catch (Exception ignored) {}
            try { tts.shutdown(); } catch (Exception ignored) {}
        }
    }
}
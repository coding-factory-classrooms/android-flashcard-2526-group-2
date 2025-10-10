package com.example.flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Adapter avec 2 types : section (en-tête) et question */
public class QuestionsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Listener {
        void onSpeak(String text);
        void onQuestionClick(Item.QuestionItem item);
    }

    // --- Modèle linéaire aplati (sections + questions)
    public static abstract class Item {
        public static class SectionItem extends Item {
            public final String title; public SectionItem(String t){ this.title = t; }
        }
        public static class QuestionItem extends Item {
            public final String difficulty;
            public final String q, c0, c1, c2, answer;
            public QuestionItem(String diff, String[] arr){
                this.difficulty = diff;
                this.q = arr[0]; this.c0 = arr[1]; this.c1 = arr[2]; this.c2 = arr[3]; this.answer = arr[4];
            }
        }
    }

    private static final int VT_SECTION = 0;
    private static final int VT_QUESTION = 1;

    private final List<Item> items;
    private final Listener listener;

    public QuestionsListAdapter(List<Item> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override public int getItemViewType(int position) {
        return (items.get(position) instanceof Item.SectionItem) ? VT_SECTION : VT_QUESTION;
    }

    @NonNull
    @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == VT_SECTION) {
            View v = inf.inflate(R.layout.item_section_header, parent, false);
            return new SectionVH(v);
        } else {
            View v = inf.inflate(R.layout.item_question, parent, false);
            return new QuestionVH(v);
        }
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        if (holder instanceof SectionVH) {
            ((SectionVH) holder).bind(((Item.SectionItem) item).title);
        } else {
            ((QuestionVH) holder).bind((Item.QuestionItem) item, listener);
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class SectionVH extends RecyclerView.ViewHolder {
        private final TextView tv;
        SectionVH(View v){ super(v); tv = v.findViewById(R.id.sectionTitle); }
        void bind(String t){ tv.setText(t); }
    }

    static class QuestionVH extends RecyclerView.ViewHolder {
        private final TextView tvQuestion;
        private final ImageButton btnSpeak;
        QuestionVH(View v){
            super(v);
            tvQuestion = v.findViewById(R.id.tvQuestion);
            btnSpeak   = v.findViewById(R.id.btnSpeak);
        }
        void bind(Item.QuestionItem item, Listener listener){
            tvQuestion.setText(item.q);
            // Tap sur la ligne => tester cette question dans GameActivity
            itemView.setOnClickListener(v -> listener.onQuestionClick(item));
            // Micro => lire la question
            btnSpeak.setOnClickListener(v -> listener.onSpeak(item.q));
        }
    }
}
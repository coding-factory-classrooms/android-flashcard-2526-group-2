package com.example.flashcard;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Charge les questions depuis res/raw/questions.json */
public final class QuestionsJSON {

    private QuestionsJSON() {}

    public static List<String[]> getQuestions(Context ctx, String difficulty) {
        List<String[]> out = new ArrayList<>();
        if (difficulty == null) return out;

        try {
            // 1) lire le fichier raw en String
            InputStream is = ctx.getResources().openRawResource(R.raw.question);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            // 2) parser
            JSONObject root = new JSONObject(sb.toString());
            JSONArray arr = root.optJSONArray(difficulty.toLowerCase());
            if (arr == null) return out;

            // 3) convertir en List<String[]> au format attendu:
            // [ question, choice0, choice1, choice2, answer ]
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String q = obj.getString("q");
                JSONArray choices = obj.getJSONArray("choices");
                String c0 = choices.getString(0);
                String c1 = choices.getString(1);
                String c2 = choices.getString(2);
                String answer = obj.getString("answer");
                out.add(new String[]{ q, c0, c1, c2, answer });
            }
        } catch (Exception e) {
            e.printStackTrace(); // KISS : log si souci
        }
        return out;
    }
}
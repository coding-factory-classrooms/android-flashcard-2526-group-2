package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

public class Question {

    public static List<String[]> getQuestions(String difficulty) {
        List<String[]> questions = new ArrayList<>();

        if ("facile".equalsIgnoreCase(difficulty)) {
            questions.add(new String[]{
                    "Comment s'appelle le père de la famille ?",
                    "Homer",
                    "Bart",
                    "Marge",
                    "Homer"
            });
            questions.add(new String[]{
                    "Comment s'appelle la fille ?",
                    "Lisa",
                    "Maggie",
                    "Marge",
                    "Lisa"
            });
            questions.add(new String[]{
                    "Comment s'appelle la mère ?",
                    "Marge",
                    "Maggie",
                    "Patty",
                    "Marge"
            });

        } else if ("moyen".equalsIgnoreCase(difficulty)) {
            questions.add(new String[]{
                    "Quel est le travail de M. Burns ?",
                    "Propriétaire de la centrale nucléaire",
                    "Professeur",
                    "Médecin",
                    "Propriétaire de la centrale nucléaire"
            });
            questions.add(new String[]{
                    "Comment s'appelle le fils aîné ?",
                    "Bart",
                    "Lisa",
                    "Maggie",
                    "Bart"
            });
            questions.add(new String[]{
                    "Qui est le meilleur ami de Bart ?",
                    "Milhouse",
                    "Nelson",
                    "Ralph",
                    "Milhouse"
            });

        } else if ("impossible".equalsIgnoreCase(difficulty)) {
            questions.add(new String[]{
                    "Quel est le vrai nom de Krusty le clown ?",
                    "Herschel Shmoikel Pinchas Yerucham Krustofski",
                    "Herschel Krustofski",
                    "Krusty Simpson",
                    "Herschel Shmoikel Pinchas Yerucham Krustofski"
            });
            questions.add(new String[]{
                    "Comment s'appelle la mère de Ned Flanders ?",
                    "Mona",
                    "Maude",
                    "Tara",
                    "Mona"
            });
            questions.add(new String[]{
                    "Quel est le nom de la boîte de donuts préférée de Homer ?",
                    "Lard Lad Donuts",
                    "Sprinkles Donuts",
                    "Sweet Dough",
                    "Lard Lad Donuts"
            });
            questions.add(new String[]{
                    "Quel est le nom du chien de la famille Flanders ?",
                    "Apu",
                    "Santa's Little Helper",
                    "Fido",
                    "Santa's Little Helper"
            });
            questions.add(new String[]{
                    "Quelle est la marque de bière que Homer préfère ?",
                    "Duff Beer",
                    "Buzz Beer",
                    "Springfield Ale",
                    "Duff Beer"
            });

        }

        return questions;
    }
}

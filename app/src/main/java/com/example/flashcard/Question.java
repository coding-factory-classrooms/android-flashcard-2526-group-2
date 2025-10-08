package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

public class Question {

    public static List<String[]> getQuestions(String difficulty) {
        List<String[]> questions = new ArrayList<>();
        if (difficulty == null) return questions;

        switch (difficulty.toLowerCase()) {
            case "facile":
                questions.add(new String[]{"Comment s'appelle le père de la famille Simpson ?", "Homer", "Bart", "Ned", "Homer"});
                questions.add(new String[]{"Quel est le prénom de la mère ?", "Marge", "Mona", "Selma", "Marge"});
                questions.add(new String[]{"Quel est le prénom du fils aîné ?", "Bart", "Milhouse", "Ralph", "Bart"});
                questions.add(new String[]{"Comment s'appelle la petite dernière ?", "Maggie", "Lisa", "Janey", "Maggie"});
                questions.add(new String[]{"Qui joue du saxophone ?", "Lisa", "Bart", "Marge", "Lisa"});
                questions.add(new String[]{"Qui tient le bar emblématique de Springfield ?", "Moe", "Apu", "Barney", "Moe"});
                questions.add(new String[]{"Quelle bière Homer préfère-t-il ?", "Duff", "Fudd", "Buzz Cola", "Duff"});
                questions.add(new String[]{"Où travaille Homer ?", "Centrale nucléaire", "École élémentaire", "Mairie", "Centrale nucléaire"});
                questions.add(new String[]{"Quel voisin dit souvent « Okily-dokily » ?", "Ned Flanders", "Chief Wiggum", "Skinner", "Ned Flanders"});
                questions.add(new String[]{"Comment s’appelle le clown célèbre ?", "Krusty", "Sideshow Bob", "Troy McClure", "Krusty"});
                questions.add(new String[]{"Quel est le prénom de la sœur intelligente de Bart ?", "Lisa", "Maggie", "Ling", "Lisa"});
                questions.add(new String[]{"Le magasin d’Apu s’appelle…", "Kwik-E-Mart", "Try-N-Save", "Leftorium", "Kwik-E-Mart"});
                questions.add(new String[]{"Le chat le plus souvent présent chez les Simpson est…", "Snowball II", "Snowball I", "Scratchy", "Snowball II"});
                questions.add(new String[]{"Le chien des Simpson s’appelle…", "Petit Papa Noël (Santa's Little Helper)", "Poochie", "Laddie", "Petit Papa Noël (Santa's Little Helper)"});
                questions.add(new String[]{"Qui est le proviseur de l’école ?", "Seymour Skinner", "Edna Krapabelle", "Chalmers", "Seymour Skinner"});
                questions.add(new String[]{"Qui est le présentateur du journal TV ?", "Kent Brockman", "Arnie Pye", "Birch Barlow", "Kent Brockman"});
                questions.add(new String[]{"La ville où vivent les Simpson s’appelle…", "Springfield", "Shelbyville", "Ogdenville", "Springfield"});
                questions.add(new String[]{"Le supermarché « gauchers » est tenu par…", "Ned Flanders", "Apu", "Gil", "Ned Flanders"});
                questions.add(new String[]{"Qui est le meilleur ami de Bart ?", "Milhouse", "Nelson", "Ralph", "Milhouse"});
                questions.add(new String[]{"Quelle boisson gazeuse parodie-t-on souvent ?", "Buzz Cola", "Slurm", "Squishee", "Buzz Cola"});
                break;

            case "moyen":
                questions.add(new String[]{"Quel est le nom de jeune fille de Marge ?", "Bouvier", "Hoover", "Smithers", "Bouvier"});
                questions.add(new String[]{"La mère d’Homer s’appelle…", "Mona", "Jacqueline", "Agnes", "Mona"});
                questions.add(new String[]{"Le directeur de l’école se nomme…", "Seymour Skinner", "Gary Chalmers", "Edna", "Seymour Skinner"});
                questions.add(new String[]{"Quel instrument joue Lisa ?", "Saxophone", "Guitare", "Piano", "Saxophone"});
                questions.add(new String[]{"Le bar préféré d’Homer est…", "La Taverne de Moe", "Duff Bar", "Krusty Bar", "La Taverne de Moe"});
                questions.add(new String[]{"Le présentateur météo en hélico s’appelle…", "Arnie Pye", "Kent Brockman", "Duffman", "Arnie Pye"});
                questions.add(new String[]{"La police est dirigée par…", "Chief Wiggum", "Lou", "Eddie", "Chief Wiggum"});
                questions.add(new String[]{"Le révérend de Springfield est…", "Lovejoy", "Flanders", "Hibbert", "Lovejoy"});
                questions.add(new String[]{"Le médecin riant souvent est…", "Dr. Hibbert", "Dr. Nick", "Dr. Wolfe", "Dr. Hibbert"});
                questions.add(new String[]{"Le meilleur ami alcoolisé d’Homer est…", "Barney Gumble", "Lenny", "Carl", "Barney Gumble"});
                questions.add(new String[]{"La sœur de Marge aux cheveux en boule est…", "Patty", "Selma", "Brandine", "Patty"});
                questions.add(new String[]{"Quelle chaîne vend la Duff ?", "Duff Brewery", "Buzz Factory", "Fudd Inc.", "Duff Brewery"});
                questions.add(new String[]{"Le journal local est…", "Springfield Shopper", "Springfield Times", "Daily Springfield", "Springfield Shopper"});
                questions.add(new String[]{"La TV locale diffuse souvent…", "The Itchy & Scratchy Show", "Tom & Jerry", "Ren & Stimpy", "The Itchy & Scratchy Show"});
                questions.add(new String[]{"Le meilleur ennemi de Bart à l’école est souvent…", "Nelson Muntz", "Jimbo", "Dolph", "Nelson Muntz"});
                questions.add(new String[]{"Le principal fournisseur de BD est…", "Comic Book Guy", "Gil", "Kearney", "Comic Book Guy"});
                questions.add(new String[]{"La prof de Lisa se nomme…", "Miss Hoover", "Edna Krapabelle", "Ms. Pommelhorst", "Miss Hoover"});
                questions.add(new String[]{"Qui est le patron d’Homer ?", "M. Burns", "Smithers", "Scorpio", "M. Burns"});
                questions.add(new String[]{"Comment s’appelle la bière parodiée façon mascotte ?", "Duffman", "BeerMan", "Duff Dude", "Duffman"});
                questions.add(new String[]{"La première voiture d’Homer est souvent représentée…", "Rose", "Bleue", "Verte", "Rose"});
                break;

            case "difficile":
                questions.add(new String[]{"Quel est le deuxième prénom d’Homer révélé tardivement ?", "Jay", "Joe", "Jon", "Jay"});
                questions.add(new String[]{"Le véritable nom de famille de Comic Book Guy est…", "Jeff Albertson", "Jeff Albright", "Jeff Harrison", "Jeff Albertson"});
                questions.add(new String[]{"Le vrai nom de Sideshow Bob est…", "Robert Terwilliger", "Robert Wigum", "Robert McBain", "Robert Terwilliger"});
                questions.add(new String[]{"Le groupe d’Homer qui chante « Baby on Board » est…", "The Be Sharps", "The D’oh Notes", "The Dufftones", "The Be Sharps"});
                questions.add(new String[]{"Le chef cuisinier français caricatural apparaît comme…", "Chef Français (gag récurrent)", "César", "Anton Ego", "Chef Français (gag récurrent)"});
                questions.add(new String[]{"Le prénom de la femme de Ned décédée est…", "Maude", "Mona", "Mary", "Maude"});
                questions.add(new String[]{"L’entrepreneur excentrique de Globex Corp. est…", "Hank Scorpio", "Arthur Fortune", "Rich Texan", "Hank Scorpio"});
                questions.add(new String[]{"Le duo ‘Itchy & Scratchy’ est produit parfois par…", "Krusty Studios", "Duff Studios", "KBBL", "Krusty Studios"});
                questions.add(new String[]{"Le jazzman mentor de Lisa est…", "Bleeding Gums Murphy", "Murphy Jazz", "Bleeding Gum", "Bleeding Gums Murphy"});
                questions.add(new String[]{"Le supérieur de Skinner est…", "Chalmers", "Hoover", "Largo", "Chalmers"});
                questions.add(new String[]{"Le scientifique loufoque à lunettes est…", "Professeur Frink", "Dr. Nick", "Dr. Colossus", "Professeur Frink"});
                questions.add(new String[]{"La mascotte géante de donuts s’appelle…", "Lard Lad", "Duff Lad", "Donut Boy", "Lard Lad"});
                questions.add(new String[]{"L’épisode parodie les sociétés secrètes via…", "Stonecutters", "Freemasons", "Illuminati", "Stonecutters"});
                questions.add(new String[]{"Le voisin millionnaire au gros rire est…", "Rich Texan", "Arthur Fortune", "Wolfcastle", "Rich Texan"});
                questions.add(new String[]{"Le nom de scène de Rainier Wolfcastle est associé à…", "McBain", "McPain", "Hardcastle", "McBain"});
                questions.add(new String[]{"Le prénom de la fille d’Apu est (un exemple parmi la fratrie)…", "Priti", "Maya", "Anu", "Priti"});
                questions.add(new String[]{"Le nom de la ligne ferroviaire bidon promue par Lyle Lanley…", "Monorail", "Hyperloop", "FastRail", "Monorail"});
                questions.add(new String[]{"Le présentateur avocat gaffeur qui disparaît est…", "Lionel Hutz", "Blue Haired Lawyer", "Gil Gunderson", "Lionel Hutz"});
                questions.add(new String[]{"La ville voisine et rivale principale est…", "Shelbyville", "North Haverbrook", "Capital City", "Shelbyville"});
                questions.add(new String[]{"Le patronyme de Marge est partagé avec ses sœurs :", "Bouvier", "Bouyer", "Bouvet", "Bouvier"});
                break;

            case "hardcore":
                questions.add(new String[]{"Nom complet (version la plus citée) de Krusty le Clown :", "Herschel Krustofski", "Herschel Shmoikel Krustofski", "Hyman Krustofski", "Herschel Krustofski"});
                questions.add(new String[]{"Le vrai nom du proviseur révélé dans un épisode :", "Armin Tamzarian", "Seymour Tamzarian", "Armin Terwilliger", "Armin Tamzarian"});
                questions.add(new String[]{"Le nom de l’usine où travaille Homer :", "Centrale de Springfield", "Centrale de Shelbyville", "Nuclear Town", "Centrale de Springfield"});
                questions.add(new String[]{"Le nom du talk-show de Krusty quand il se « réinvente » :", "Krusty Komedy Klassic", "Krusty Comeback", "Krusty Live", "Krusty Komedy Klassic"});
                questions.add(new String[]{"Le prénom du père de Milhouse :", "Kirk", "Kirkland", "Kip", "Kirk"});
                questions.add(new String[]{"Le prénom de la mère de Milhouse :", "Luann", "Luanne", "Luan", "Luann"});
                questions.add(new String[]{"Le nom du super-héros local parodié incarné en film :", "Radioactive Man", "Fallout Man", "Nuclear Man", "Radioactive Man"});
                questions.add(new String[]{"Le nom de l’acolyte de Radioactive Man :", "Fallout Boy", "Sidekick Boy", "Atom Kid", "Fallout Boy"});
                questions.add(new String[]{"La boutique pour gauchers de Flanders :", "Leftorium", "LeftPlace", "LeftyShop", "Leftorium"});
                questions.add(new String[]{"Le prénom du professeur de musique de l’école :", "Dewey Largo", "Largo Dewey", "Larry Dew", "Dewey Largo"});
                questions.add(new String[]{"Le consultant juridique aux cheveux bleus :", "Blue Haired Lawyer", "Lionel Hutz", "Gil Gunderson", "Blue Haired Lawyer"});
                questions.add(new String[]{"L’investisseur milliardaire sympathique rencontré par Homer :", "Arthur Fortune", "Rich Texan", "Hank Scorpio", "Arthur Fortune"});
                questions.add(new String[]{"Le médecin à l’accent ‘Hi everybody!’ :", "Dr. Nick Riviera", "Dr. Hibbert", "Dr. Wolfe", "Dr. Nick Riviera"});
                questions.add(new String[]{"Le présentateur radio conservateur de Springfield :", "Birch Barlow", "Kent Brockman", "Arnie Pye", "Birch Barlow"});
                questions.add(new String[]{"Le nom du propriétaire de la station KBBL :", "KBBL", "KABL", "KLBL", "KBBL"});
                questions.add(new String[]{"La devise secrète chantée des Stonecutters (« We Do ») fait référence à :", "Stonecutters", "Be Sharps", "Monorailers", "Stonecutters"});
                questions.add(new String[]{"Le nom du vendeur malchanceux qui foire tout :", "Gil Gunderson", "Old Gil", "Gilly Anderson", "Gil Gunderson"});
                questions.add(new String[]{"La ville vantée par le charlatan du Monorail :", "North Haverbrook", "Ogdenville", "Shelbyville", "North Haverbrook"});
                questions.add(new String[]{"Le nom de l’acteur parodiant Schwarzenegger :", "Rainier Wolfcastle", "McBain Wolf", "Rainer Castle", "Rainier Wolfcastle"});
                questions.add(new String[]{"Le prénom de la fille de Flanders qui apparaît dans certains médias dérivés :", "Aucune fille canon", "Maude Jr.", "Nancy", "Aucune fille canon"});
                break;

            case "impossible":
                questions.add(new String[]{"Nom « officiel » le plus long attribué à Krusty :", "Herschel Shmoikel Pinchas Yerucham Krustofski", "Herschel Shmoikel Krustofski", "Hyman Herschel Krustofski", "Herschel Shmoikel Pinchas Yerucham Krustofski"});
                questions.add(new String[]{"Le nom du journal local :", "Springfield Shopper", "Springfield Times", "Springfield Tribune", "Springfield Shopper"});
                questions.add(new String[]{"Le propriétaire de la boutique de BD :", "Comic Book Guy (Jeff Albertson)", "Jefferson Comics", "Guy the Comic", "Comic Book Guy (Jeff Albertson)"});
                questions.add(new String[]{"La boisson givrée du Kwik-E-Mart :", "Squishee", "Slushee", "Squishy", "Squishee"});
                questions.add(new String[]{"La ville rivale ‘jumelle’ de Springfield :", "Shelbyville", "Capital City", "Cypress Creek", "Shelbyville"});
                questions.add(new String[]{"Le ‘patron’ de Smithers :", "M. Burns", "M. Scorpio", "M. Teeny", "M. Burns"});
                questions.add(new String[]{"Le groupe d’Homer dans l’épisode barbershop :", "The Be Sharps", "The Sharp Be", "Barbertones", "The Be Sharps"});
                questions.add(new String[]{"Le pseudo du vendeur de BD :", "Comic Book Guy", "CBG", "Jeff", "Comic Book Guy"});
                questions.add(new String[]{"Le prénom du présentateur juridique glacial :", "— (souvent sans prénom)", "James", "Robert", "— (souvent sans prénom)"});
                questions.add(new String[]{"La station TV locale est :", "Channel 6", "Channel 7", "KBBL-TV", "Channel 6"});
                questions.add(new String[]{"La bande rivale d’ados dont Jimbo fait partie :", "Les gros durs du lycée (générique)", "The Bullies", "The Tough Boys", "Les gros durs du lycée (générique)"});
                questions.add(new String[]{"La marque du donut géant :", "Lard Lad Donuts", "Duff Donuts", "Mega Donut", "Lard Lad Donuts"});
                questions.add(new String[]{"Le pseudo du critique culinaire véhément :", "The Food Critic (var. Freddy Quimby gag)", "Monty Critic", "Judge Food", "The Food Critic (var. Freddy Quimby gag)"});
                questions.add(new String[]{"Le prénom du maire Quimby :", "Joe", "Bob", "Dan", "Joe"});
                questions.add(new String[]{"Le vrai nom de ‘Sideshow Mel’ :", "Melvin Van Horne", "Melvin Horne", "Melvin Terwilliger", "Melvin Van Horne"});
                questions.add(new String[]{"Le nom de la ville ‘idéale’ où Homer travaille pour Scorpio :", "Cypress Creek", "Shelbyville Heights", "North Haverbrook", "Cypress Creek"});
                questions.add(new String[]{"Le nom complet de Patty/Selma (famille) :", "Patty et Selma Bouvier", "Patty et Selma Smith", "Patty et Selma Hoover", "Patty et Selma Bouvier"});
                questions.add(new String[]{"Le prénom du fils de Ned plus pieux :", "Rod", "Todd", "Rob", "Rod"});
                questions.add(new String[]{"Le prénom du fils de Ned plus jeune :", "Todd", "Ted", "Tom", "Todd"});
                questions.add(new String[]{"L’entrepreneur du Monorail :", "Lyle Lanley", "Lionel Hutz", "Lyle Langley (var.)", "Lyle Lanley"});
                break;

            default:
                break;
        }

        return questions;
    }
}

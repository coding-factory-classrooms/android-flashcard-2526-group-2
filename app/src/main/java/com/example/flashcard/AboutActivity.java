package com.example.flashcard;

import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        // --- Récupération et affichage de la version ---
        TextView textVersion = findViewById(R.id.text_version);
        String versionName = "N/A";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            Log.i("TAG", "Version : " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textVersion.setText("Version " + versionName);

        // Version du contour en noir
        TextView textVerOutline = findViewById(R.id.textView_ver_outline);
        String versionNameOutline = "N/A";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNameOutline = pInfo.versionName;
            Log.i("TAG", "Version : " + versionNameOutline);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textVerOutline.setText("Version " + versionName);

        // --- Bouton retour ---
        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> {
            // Ferme simplement cette activité pour revenir à la précédente
            finish();

            TextView LT = findViewById(R.id.textView5);
            TextView AA = findViewById(R.id.textView3);
            TextView FN = findViewById(R.id.textView9);

            // Un son pour chaque
            MediaPlayer soundLT = MediaPlayer.create(this, R.raw.nelson_haha);
            MediaPlayer soundAA = MediaPlayer.create(this, R.raw.nelson_haha);
            MediaPlayer soundFN = MediaPlayer.create(this, R.raw.nelson_haha);

            LT.setOnClickListener(v1 -> {
                soundLT.start();
            });

            AA.setOnClickListener(v2 -> {
                soundAA.start();
            });

            FN.setOnClickListener(v3 -> {
                soundFN.start();
            });

        });
    }
}
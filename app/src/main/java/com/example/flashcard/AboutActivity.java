package com.example.flashcard;

import com.example.flashcard.AudioKit;
import com.example.flashcard.HomeActivity;
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
        textVersion.setText("Version : " + versionName);

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
        textVerOutline.setText("Version : " + versionName);


        // --- Bouton retour ---
        ImageButton buttonBack = findViewById(R.id.button_back);

        // Hover du bouton retour
        HomeActivity.setupHoverScale(buttonBack, 1.2f);

        buttonBack.setOnClickListener(v -> {
            // Ferme simplement cette activité pour revenir à la précédente
            finish();
        });
    }
}
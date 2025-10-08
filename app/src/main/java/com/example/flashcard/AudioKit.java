// AudioKit.java
package com.example.flashcard;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

/**
 * AudioKit
 * Classe utilitaire simple (KISS) pour gérer :
 * - une musique de fond (BGM) via MediaPlayer (pause/reprise/stop/volume, boucle possible)
 * - des effets sonores courts (SFX) via SoundPool (plusieurs sons en même temps)
 *
 * Tous les sons doivent être placés dans res/raw/ (ex. R.raw.theme_music, R.raw.click).
 */
public final class AudioKit {

    // --------- État de la musique de fond (BGM) ---------
    private static MediaPlayer bgm;
    private static int bgmLastPos = 0; // position sauvegardée pour reprise après pause

    // --------- État des effets sonores (SFX) ---------
    private static SoundPool sfxPool;
    private static final SparseIntArray sfxCache = new SparseIntArray(); // map: resId -> soundId
    private static boolean sfxInited = false;

    private AudioKit() {
        // Classe utilitaire : pas d'instanciation
    }

    // ---------- BGM ----------

    /**
     * Démarre la musique de fond à partir d'une ressource raw.
     * Si une BGM tournait déjà, elle est stoppée et remplacée.
     *
     * @param ctx   Contexte (utiliser l'ApplicationContext)
     * @param resId Ressource audio (ex : R.raw.theme_music)
     * @param loop  true pour boucler en continu
     */
    public static void startBgm(Context ctx, int resId, boolean loop) {
        stopBgm(); // nettoie si déjà en cours
        bgm = MediaPlayer.create(ctx.getApplicationContext(), resId);
        if (bgm == null) return;
        bgm.setLooping(loop);
        bgmLastPos = 0;
        bgm.start();
    }

    /**
     * Met en pause la BGM et sauvegarde la position courante pour pouvoir reprendre plus tard.
     */
    public static void pauseBgm() {
        if (bgm != null && bgm.isPlaying()) {
            bgmLastPos = bgm.getCurrentPosition();
            bgm.pause();
        }
    }

    /**
     * Reprend la BGM là où elle avait été mise en pause.
     */
    public static void resumeBgm() {
        if (bgm != null) {
            bgm.seekTo(bgmLastPos);
            bgm.start();
        }
    }

    /**
     * Arrête et libère complètement la BGM.
     * Après cet appel, il faudra relancer startBgm(...) pour rejouer.
     */
    public static void stopBgm() {
        if (bgm != null) {
            try { bgm.stop(); } catch (IllegalStateException ignored) {
                // Si stop() est appelé dans un mauvais état, on ignore et on libère quand même.
            }
            bgm.reset();
            bgm.release();
            bgm = null;
            bgmLastPos = 0;
        }
    }

    /**
     * Règle le volume de la BGM.
     *
     * @param left  volume canal gauche (0.0f à 1.0f)
     * @param right volume canal droit  (0.0f à 1.0f)
     */
    public static void setBgmVolume(float left, float right) {
        if (bgm != null) bgm.setVolume(left, right);
    }

    // ---------- SFX ----------

    /**
     * Initialise SoundPool si nécessaire (une seule fois).
     * Permet de jouer plusieurs sons courts en parallèle avec faible latence.
     */
    private static void ensureSfx() {
        if (sfxInited) return;
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sfxPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(8) // permet 8 sons simultanés
                .build();
        sfxInited = true;
    }

    /**
     * Précharge un effet sonore pour qu'il soit prêt à jouer immédiatement.
     *
     * @param ctx   Contexte
     * @param resId Ressource audio (ex : R.raw.click)
     */
    public static void preloadSfx(Context ctx, int resId) {
        ensureSfx();
        if (sfxCache.get(resId, -1) == -1) {
            int id = sfxPool.load(ctx.getApplicationContext(), resId, 1);
            sfxCache.put(resId, id);
        }
    }

    /**
     * Joue un effet sonore. S'il n'est pas encore chargé, on le charge puis on le joue dès qu'il est prêt.
     * Volume par défaut : 1.0f gauche/droite, pas de boucle, vitesse 1.0.
     *
     * @param ctx   Contexte
     * @param resId Ressource audio (ex : R.raw.click)
     */
    public static void playSfx(Context ctx, int resId) {
        ensureSfx();
        int id = sfxCache.get(resId, -1);
        if (id == -1) { // pas encore chargé
            id = sfxPool.load(ctx.getApplicationContext(), resId, 1);
            sfxCache.put(resId, id);
            int finalId = id;
            sfxPool.setOnLoadCompleteListener((pool, sampleId, status) -> {
                if (status == 0 && sampleId == finalId) {
                    pool.play(finalId, 1f, 1f, 1, 0, 1f);
                }
            });
        }
        // Si déjà en cache, le son part immédiatement :
        sfxPool.play(id, 1f, 1f, 1, 0, 1f);
    }

    /**
     * Libère toutes les ressources liées aux effets sonores (SoundPool + cache).
     * À appeler par exemple dans onDestroy() de l'Activity.
     */
    public static void releaseSfx() {
        if (sfxPool != null) {
            sfxPool.release();
            sfxPool = null;
            sfxCache.clear();
            sfxInited = false;
        }
    }

    public static void playLongOnce(Context ctx, int resId) {
        MediaPlayer mp = MediaPlayer.create(ctx.getApplicationContext(), resId);
        if (mp == null) return;
        mp.setOnCompletionListener(MediaPlayer::release); // libère automatiquement à la fin
        mp.start();
    }

    // ---------- Tout couper ----------

    /**
     * Coupe et libère tout l'audio : BGM + SFX.
     * Pratique pour nettoyer proprement à la fermeture d'un écran ou de l'app.
     */
    public static void releaseAll() {
        stopBgm();
        releaseSfx();
    }
}

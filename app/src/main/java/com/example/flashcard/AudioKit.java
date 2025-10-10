// AudioKit.java
package com.example.flashcard;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

public final class AudioKit {


    private static MediaPlayer bgm;
    private static int bgmLastPos = 0;

    private static SoundPool sfxPool;
    private static final SparseIntArray sfxCache = new SparseIntArray();
    private static boolean sfxInited = false;

    private AudioKit() {
    }


    // permet de start une musique en Back ground
    public static void startBgm(Context ctx, int resId, boolean loop) {
        stopBgm(); // nettoie si déjà en cours
        bgm = MediaPlayer.create(ctx.getApplicationContext(), resId);
        if (bgm == null) return;
        bgm.setLooping(loop);
        bgmLastPos = 0;
        bgm.start();
    }

    // met pause bgm
    public static void pauseBgm() {
        if (bgm != null && bgm.isPlaying()) {
            bgmLastPos = bgm.getCurrentPosition();
            bgm.pause();
        }
    }

    // Reprend la BGM là où elle avait été mise en pause.

    public static void resumeBgm() {
        if (bgm != null) {
            bgm.seekTo(bgmLastPos);
            bgm.start();
        }
    }

    // arrête bgm et libère la mémoire
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


    // modifie le volume du bgm (son gauche, son droit)
    public static void setBgmVolume(float left, float right) {
        if (bgm != null) bgm.setVolume(left, right);
    }

    // ---------- SFX ----------

    // permet d'avoir plusieurs son en même temps
    private static void ensureSfx() {
        if (sfxInited) return;
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sfxPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(8) // permet 8 sons en même temps
                .build();
        sfxInited = true;
    }


    // permet de précharger, pas obligatoire mais utile
    public static void preloadSfx(Context ctx, int resId) {
        ensureSfx();
        if (sfxCache.get(resId, -1) == -1) {
            int id = sfxPool.load(ctx.getApplicationContext(), resId, 1);
            sfxCache.put(resId, id);
        }
    }

     // play un sfx
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

    // stop sfx
    public static void releaseSfx() {
        if (sfxPool != null) {
            sfxPool.release();
            sfxPool = null;
            sfxCache.clear();
            sfxInited = false;
        }
    }

    // joue un sfx longue durée
    public static void playLongOnce(Context ctx, int resId) {
        MediaPlayer mp = MediaPlayer.create(ctx.getApplicationContext(), resId);
        if (mp == null) return;
        mp.setOnCompletionListener(MediaPlayer::release); // libère automatiquement à la fin
        mp.start();
    }

    // ---------- Tout couper ----------

    // coupe tout (bgm + sfx)
    public static void releaseAll() {
        stopBgm();
        releaseSfx();
    }
}

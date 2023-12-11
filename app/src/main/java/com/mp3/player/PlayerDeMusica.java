package com.mp3.player;

import android.media.MediaPlayer;

public class PlayerDeMusica {
    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }
    public static int index = -1;
}
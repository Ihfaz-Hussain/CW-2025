package com.comp2042;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class AudioManager {

    private static MediaPlayer bgPlayer;

    // Initialise the media player once
    public static void initBackgroundMusic() {
        if (bgPlayer != null) return; // already initialised

        // Load music from resources/audio/tetris_theme.mp3
        var resource = AudioManager.class.getResource("/audio/tetris_theme.mp3");
        if (resource == null) {
            System.err.println("Could not find background music file!");
            return;
        }

        Media media = new Media(resource.toString());
        bgPlayer = new MediaPlayer(media);
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever
        bgPlayer.setVolume(0.4); // 40% volume, tweak as you like
    }

    public static void playBackground() {
        if (bgPlayer == null) initBackgroundMusic();
        if (bgPlayer != null) {
            bgPlayer.play();
        }
    }

    public static void pauseBackground() {
        if (bgPlayer != null) {
            bgPlayer.pause();
        }
    }

    public static boolean isPlaying() {
        return bgPlayer != null &&
                bgPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}

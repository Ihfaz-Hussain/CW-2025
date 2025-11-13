package com.comp2042;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {

    // This is our global background music player
    private static Clip bgClip;

    /**
     * Load the background music file once.
     * Uses Java's built-in audio (no extra libraries).
     */
    public static void initBackgroundMusic() {
        // If already loaded, do nothing
        if (bgClip != null) {
            return;
        }

        try {
            // 1. Find the file inside resources:
            //    src/main/resources/audio/tetris_theme.wav
            URL soundUrl = AudioManager.class.getResource("/audio/tetris_theme.wav");

            if (soundUrl == null) {
                System.err.println("Background music file not found: /audio/tetris_theme.wav");
                return;
            }

            // 2. Open it as an audio stream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);

            // 3. Get a Clip (small audio player) from the system
            bgClip = AudioSystem.getClip();

            // 4. Load the audio data into the Clip
            bgClip.open(audioIn);

            // 5. Loop forever when played
            bgClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio format not supported. Use a WAV (PCM) file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable on this system.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading audio file.");
            e.printStackTrace();
        }
    }

    /**
     * Start / resume playing the background music.
     */
    public static void playBackground() {
        if (bgClip == null) {
            initBackgroundMusic();
        }

        if (bgClip != null) {
            // If clip is at end, rewind to start
            if (!bgClip.isRunning()) {
                bgClip.setFramePosition(0); // start from beginning
            }
            bgClip.start();
        }
    }

    /**
     * Pause the background music (without resetting position).
     */
    public static void pauseBackground() {
        if (bgClip != null && bgClip.isRunning()) {
            bgClip.stop();
        }
    }

    /**
     * Check if the background music is currently playing.
     */
    public static boolean isPlaying() {
        return bgClip != null && bgClip.isRunning();
    }
}

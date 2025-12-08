package com.comp2042.view;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game timer that controls the falling speed of bricks.
 * Handles pause/resume and adjusts speed based on the current level.
 */
public class GameTimer {

    private Timeline timeline;
    private final InputEventListener eventListener;
    private final Runnable onTick;
    private boolean isPaused = false;

    /**
     * Creates a new GameTimer with the specified event listener and tick callback.
     *
     * @param eventListener the listener for game events
     * @param onTick        the callback to execute on each timer tick
     */
    public GameTimer(InputEventListener eventListener, Runnable onTick) {
        this.eventListener = eventListener;
        this.onTick = onTick;
        initializeTimeline();
    }

    /**
     * Initializes the timeline with a 400ms keyframe interval.
     */
    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the game timer if not paused.
     */
    public void start() {
        if (timeline != null && !isPaused) {
            timeline.play();
        }
    }

    /**
     * Stops the game timer completely.
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Pauses the game timer.
     */
    public void pause() {
        isPaused = true;
        stop();
    }

    /**
     * Resumes the game timer from a paused state.
     */
    public void resume() {
        isPaused = false;
        start();
    }

    /**
     * Updates the timer speed based on the current game level.
     * Higher levels result in faster falling bricks.
     *
     * @param level the current game level (1 or higher)
     */
    public void updateSpeedForLevel(int level) {
        if (timeline == null) {
            return;
        }

        // every level increases rate by 0.2, minimum rate 1.0
        double rate = 1.0 + (level - 1) * 0.2;

        // Optional: clamp to avoid insane speeds
        if (rate > 4.0) {
            rate = 4.0;
        }

        timeline.setRate(rate);
    }

    /**
     * Checks if the timer is currently paused.
     *
     * @return {@code true} if paused, {@code false} otherwise
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Sets the paused state of the timer.
     *
     * @param paused {@code true} to pause, {@code false} to unpause
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
}

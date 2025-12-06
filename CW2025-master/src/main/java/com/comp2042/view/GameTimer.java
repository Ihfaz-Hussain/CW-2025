package com.comp2042.view;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    
    private Timeline timeline;
    private final InputEventListener eventListener;
    private final Runnable onTick;
    private boolean isPaused = false;
    
    public GameTimer(InputEventListener eventListener, Runnable onTick) {
        this.eventListener = eventListener;
        this.onTick = onTick;
        initializeTimeline();
    }
    
    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> onTick.run()
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    public void start() {
        if (timeline != null && !isPaused) {
            timeline.play();
        }
    }
    
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    
    public void pause() {
        isPaused = true;
        stop();
    }
    
    public void resume() {
        isPaused = false;
        start();
    }
    
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
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
}

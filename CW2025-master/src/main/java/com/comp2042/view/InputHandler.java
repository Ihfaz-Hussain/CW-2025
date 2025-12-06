package com.comp2042.view;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    
    private final Map<KeyCode, Runnable> keyActions;
    private InputEventListener eventListener;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private GuiController guiController;
    
    public InputHandler() {
        this.keyActions = new HashMap<>();
        setupKeyActions();
    }
    
    private void setupKeyActions() {
        keyActions.put(KeyCode.LEFT,
                () -> handleMoveWithRefresh(() -> eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))));
        keyActions.put(KeyCode.A, keyActions.get(KeyCode.LEFT));

        keyActions.put(KeyCode.RIGHT,
                () -> handleMoveWithRefresh(() -> eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))));
        keyActions.put(KeyCode.D, keyActions.get(KeyCode.RIGHT));

        keyActions.put(KeyCode.UP,
                () -> handleMoveWithRefresh(() -> eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))));
        keyActions.put(KeyCode.W, keyActions.get(KeyCode.UP));

        keyActions.put(KeyCode.DOWN, 
                () -> {
                    if (!isPaused && !isGameOver && guiController != null) {
                        guiController.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    }
                });
        keyActions.put(KeyCode.S, keyActions.get(KeyCode.DOWN));

        keyActions.put(KeyCode.SPACE, 
                () -> {
                    if (!isPaused && !isGameOver && guiController != null) {
                        guiController.hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                    }
                });

        keyActions.put(KeyCode.SHIFT,
                () -> handleMoveWithRefresh(() -> eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER))));
        keyActions.put(KeyCode.C, keyActions.get(KeyCode.SHIFT));
    }
    
    private void handleMoveWithRefresh(java.util.function.Supplier<com.comp2042.model.ViewData> moveAction) {
        if (!isPaused && !isGameOver && eventListener != null && guiController != null) {
            com.comp2042.model.ViewData viewData = moveAction.get();
            guiController.refreshBrick(viewData);
        }
    }
    
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
    
    public void setGuiController(GuiController guiController) {
        this.guiController = guiController;
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public Runnable getActionForKey(KeyCode keyCode) {
        return keyActions.get(keyCode);
    }
    
    public Map<KeyCode, Runnable> getKeyActions() {
        return new HashMap<>(keyActions);
    }
}

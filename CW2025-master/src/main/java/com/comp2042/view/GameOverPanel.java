package com.comp2042.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Game over panel that displays when the game ends.
 * Shows a styled "GAME OVER" message centered in the panel.
 * This panel is typically overlaid on the game board when the game terminates.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Creates a new game over panel with default styling.
     * The panel displays "GAME OVER" text with CSS styling applied.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}

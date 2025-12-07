package com.comp2042.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Handles the visual shake animation for the game board.
 * <p>
 * This class provides a static method to trigger a short left-and-right shaking
 * effect
 * on a given JavaFX node. It is used to give visual feedback when lines are
 * cleared.
 * </p>
 */
public class BoardShake {

    /**
     * Plays a short horizontal shake animation on the specified node.
     * <p>
     * This method applies a {@link TranslateTransition} that moves the node
     * slightly
     * to the left and right (approx 6 pixels) over a duration of 300ms. It uses
     * auto-reverse to ensuring the node returns to its original position after the
     * animation completes.
     * </p>
     *
     * @param boardNode The JavaFX node to animate (e.g., the game board pane).
     */
    public static void play(Node boardNode) {
        if (boardNode == null)
            return;

        TranslateTransition tt = new TranslateTransition(Duration.millis(50), boardNode);
        tt.setByX(6); // move right by 6 pixels
        tt.setCycleCount(6); // back and forth 3 times (6 cycles)
        tt.setAutoReverse(true); // return to start each cycle (relative)

        // Actually, simple cycle with autoReverse logic for a shake:
        // often better to go left then right or 0 -> right -> 0.
        // Let's do: 0 -> 6 -> 0 (via autoReverse) repeated.

        // Refined approach for a "shake":
        // Duration of one "shake" (center -> right)
        tt.setDuration(Duration.millis(50));
        tt.setFromX(0);
        tt.setToX(6);
        tt.setCycleCount(6); // 3 full shakes (0->6->0, 0->6->0, 0->6->0)
        tt.setAutoReverse(true);

        tt.play();
    }
}

package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks the player's score, total lines cleared, and current level.
 * Provides observable properties for UI binding.
 */
public final class Score {

    // current score shown on screen
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    // Total number of lines cleared so far in the game
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);
    // Current level (starts at level 1)
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    /**
     * Gets the observable score property for UI binding.
     *
     * @return the score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds points to the current score.
     *
     * @param i the number of points to add
     */
    public void add(int i) {
        // Increase score by i
        score.setValue(score.getValue() + i);
    }

    /**
     * Gets the observable level property for UI binding.
     *
     * @return the level property
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Gets the observable total lines property for UI binding.
     *
     * @return the total lines property
     */
    public IntegerProperty totalLinesProperty() {
        return totalLines;
    }

    /**
     * Adds cleared lines to the total and updates the level accordingly.
     * Every 5 lines cleared increases the level by 1.
     *
     * @param lines the number of lines cleared
     */
    public void addLines(int lines) {
        if (lines <= 0)
            return;

        int newTotal = totalLines.get() + lines;
        totalLines.set(newTotal);

        // every 5 lines = +1 level
        int newLevel = 1 + (newTotal / 5);
        // Do not allow level to drop below 1
        if (newLevel < 1)
            newLevel = 1;
        level.set(newLevel);
    }

    /**
     * Resets the score, lines, and level to starting values.
     */
    public void reset() {
        score.setValue(0);
        totalLines.setValue(0);
        level.setValue(1);
    }

    /**
     * Gets the current score value.
     *
     * @return the current score
     */
    public int getScore() {
        return score.get();
    }
}

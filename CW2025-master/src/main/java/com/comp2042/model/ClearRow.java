package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 * Result of a row-clearing operation.
 * Contains the number of lines removed, the updated board matrix, and bonus
 * score.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Creates a new ClearRow result.
     *
     * @param linesRemoved the number of lines that were cleared
     * @param newMatrix    the updated board matrix after clearing
     * @param scoreBonus   the bonus points awarded for clearing
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed.
     *
     * @return the count of cleared lines
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a copy of the updated board matrix.
     *
     * @return the new board matrix after clearing rows
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus for this clear operation.
     *
     * @return the bonus points awarded
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}

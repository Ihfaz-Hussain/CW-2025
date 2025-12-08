package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 * Immutable snapshot of the current game state for rendering.
 * Contains the active brick, its position, and preview data for next/hold
 * pieces.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] holdBrickData;

    /**
     * Creates a new ViewData snapshot.
     *
     * @param brickData     the current brick shape matrix
     * @param xPosition     the x (column) position of the brick
     * @param yPosition     the y (row) position of the brick
     * @param nextBrickData the next brick shape matrix
     * @param holdBrickData the held brick shape matrix, or {@code null} if none
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.holdBrickData = holdBrickData;

    }

    /**
     * Gets a copy of the current brick shape matrix.
     *
     * @return the brick shape data
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x (column) position of the brick.
     *
     * @return the column index
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y (row) position of the brick.
     *
     * @return the row index
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next brick shape matrix.
     *
     * @return the next brick shape data
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets a copy of the held brick shape matrix.
     *
     * @return the held brick shape data, or {@code null} if no brick is held
     */
    public int[][] getHoldBrickData() {
        return holdBrickData == null ? null : MatrixOperations.copy(holdBrickData);
    }
}

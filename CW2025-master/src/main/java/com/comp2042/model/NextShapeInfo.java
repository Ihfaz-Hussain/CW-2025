package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 * Represents the result of a brick rotation operation.
 * Contains the new shape matrix and the rotation position index.
 * Used to pass rotation information between game logic components.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Creates a new shape information container.
     *
     * @param shape    the brick shape matrix after rotation
     * @param position the rotation position index (0-3 for most tetrominos)
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a copy of the shape matrix.
     * Returns a defensive copy to prevent external modification.
     *
     * @return a copy of the brick shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     * The position represents the current orientation in the rotation sequence.
     *
     * @return the rotation position index
     */
    public int getPosition() {
        return position;
    }
}

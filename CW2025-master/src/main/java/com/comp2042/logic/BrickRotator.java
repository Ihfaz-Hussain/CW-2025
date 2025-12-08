package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;

/**
 * Manages the rotation state of a Tetris brick.
 * Cycles through available shape matrices when the player rotates the piece.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets the next shape in the rotation sequence without changing the current shape.
     * Useful for collision checking before actually rotating.
     *
     * @return the next shape information containing the rotated matrix and position
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the current shape matrix of the brick.
     *
     * @return the current brick shape matrix
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation position of the brick.
     * Typically used after a successful rotation.
     *
     * @param currentShape the rotation position index to set
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick and resets the rotation to the default position.
     * Called when a new brick is spawned or when switching bricks.
     *
     * @param brick the new brick to manage
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Gets the current brick being managed.
     *
     * @return the current brick instance
     */
    public Brick getBrick() {
        return brick;
    }
}

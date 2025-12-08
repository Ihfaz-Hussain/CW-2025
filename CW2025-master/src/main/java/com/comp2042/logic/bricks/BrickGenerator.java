package com.comp2042.logic.bricks;

/**
 * Factory interface for generating Tetris bricks.
 * Provides access to the current and next brick in the queue.
 */
public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}

package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Contract for a Tetris brick (tetromino).
 * Each brick provides a list of shape matrices representing its rotation
 * states.
 */
public interface Brick {

    List<int[][]> getShapeMatrix();
}

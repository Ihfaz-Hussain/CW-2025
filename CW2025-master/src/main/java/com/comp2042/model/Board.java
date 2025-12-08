package com.comp2042.model;

/**
 * Contract for the Tetris game board.
 * Defines operations for moving, rotating, and managing bricks on the playing
 * field.
 */
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    ViewData holdBrick();

    void newGame();
}

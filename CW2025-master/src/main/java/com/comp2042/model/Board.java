package com.comp2042.model;

/**
 * Contract for the Tetris game board.
 * Defines operations for moving, rotating, and managing bricks on the playing
 * field.
 */
public interface Board {

    /**
     * Moves the current brick down one row.
     *
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick one column to the left.
     *
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick one column to the right.
     *
     * @return {@code true} if the move was successful, {@code false} if blocked
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counterclockwise.
     *
     * @return {@code true} if rotation was successful, {@code false} if blocked
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the spawn position.
     *
     * @return {@code true} if game over (spawn blocked), {@code false} otherwise
     */
    boolean createNewBrick();

    /**
     * Gets the current game board matrix.
     *
     * @return 2D array representing the board state with color codes
     */
    int[][] getBoardMatrix();

    /**
     * Gets the current view data for rendering.
     *
     * @return the view data containing brick position and preview information
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the background grid.
     */
    void mergeBrickToBackground();

    /**
     * Clears any completed rows from the board.
     *
     * @return information about cleared rows and score bonus
     */
    ClearRow clearRows();

    /**
     * Gets the score tracker for this board.
     *
     * @return the Score object tracking points, lines, and level
     */
    Score getScore();

    /**
     * Holds the current brick and swaps with any previously held brick.
     *
     * @return updated view data after the hold operation
     */
    ViewData holdBrick();

    /**
     * Starts a new game by resetting the board and score.
     */
    void newGame();

    /**
     * Checks if the current brick position is in the hidden rows (top boundary).
     * Used to detect game over condition when brick locks at the top.
     *
     * @return {@code true} if any part of the brick is in hidden rows,
     *         {@code false} otherwise
     */
    boolean isBrickInHiddenRows();
}

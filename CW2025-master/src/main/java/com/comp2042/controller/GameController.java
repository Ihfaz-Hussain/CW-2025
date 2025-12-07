package com.comp2042.controller;

import com.comp2042.view.GuiController;
import com.comp2042.model.HighScoreManager;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.*;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(12, 27);

    private final GuiController viewGuiController;
    private final String playerName;

    public GameController(GuiController c) {
        this(c, "Anonymous");
    }

    public GameController(GuiController c, String playerName) {
        // Connect GUI to this controller so key presses call our methods
        viewGuiController = c;
        this.playerName = playerName;
        board.createNewBrick();
        // Connect GUI to this controller so key presses call our methods
        viewGuiController.setEventListener(this);
        // Draw the initial board + current brick
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Bind score label to score property
        viewGuiController.bindScore(board.getScore().scoreProperty());
        // Bind level label to level property
        viewGuiController.bindLevel(board.getScore().levelProperty());
        // Bind lines label to lines property
        viewGuiController.bindLines(board.getScore().totalLinesProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                // Add score based on cleared lines
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addLines(clearRow.getLinesRemoved());
            }

            // If we cannot place a new brick → game over
            if (board.createNewBrick()) {

                // Get the final score from the Score object
                int finalScore = board.getScore().getScore();

                // Tell HighScoreManager to record this score
                HighScoreManager.recordScore(finalScore);

                // Show game over panel
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        // doesn't match tetris game logic
        /*
         * else {
         * if (event.getEventSource() == EventSource.USER) {
         * board.getScore().add(1);
         * }
         * }
         */
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        ClearRow clearRow = null;

        // move the current brick down until it can't move anymore
        while (board.moveBrickDown()) {
            // no-op; board.moveBrickDown() updates currentOffset inside SimpleBoard
        }

        // Lock the brick into the background and clear completed rows
        board.mergeBrickToBackground();
        clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addLines(clearRow.getLinesRemoved());
        }

        // Spawn a new brick; if we can't, it's game over
        if (board.createNewBrick()) {
            // Save the high score with player name
            int finalScore = board.getScore().getScore();
            HighScoreManager.recordScore(playerName, finalScore);
            viewGuiController.gameOver();
        }

        // Refresh the background (so the locked brick + cleared rows are visible)
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        // Return view data for the NEW current brick
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        return board.holdBrick();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}

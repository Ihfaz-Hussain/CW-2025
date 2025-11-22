package com.comp2042;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        // Connect GUI to this controller so key presses call our methods
        viewGuiController = c;
        board.createNewBrick();
        // Connect GUI to this controller so key presses call our methods
        viewGuiController.setEventListener(this);
        // Draw the initial board + current brick
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        // Bind score label to score property
        viewGuiController.bindScore(board.getScore().scoreProperty());
        //Bind level label to level property
        viewGuiController.bindLevel(board.getScore().levelProperty());

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
                // (If you track level/lines, call that here too)
            }

            // If we cannot place a new brick → game over
            if (board.createNewBrick()) {

                // 1. Get the final score from the Score object
                int finalScore = board.getScore().getScore();

                // 2. Tell HighScoreManager to record this score
                HighScoreManager.recordScore(finalScore);

                // 3. Show game over panel
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        //doesn't match tetris game logic
        /*else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }*/
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
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}

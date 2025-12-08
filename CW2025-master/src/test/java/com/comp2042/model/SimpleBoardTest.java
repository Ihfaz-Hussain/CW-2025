package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
    }

    @Test
    void testBoardInitialization() {
        int[][] matrix = board.getBoardMatrix();
        assertEquals(20, matrix.length, "Board should have 20 rows");
        assertEquals(10, matrix[0].length, "Board should have 10 columns");
        
        // All cells should be empty initially
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                assertEquals(0, matrix[row][col], "All cells should be empty initially");
            }
        }
    }

    @Test
    void testInitialScore() {
        Score score = board.getScore();
        assertNotNull(score, "Score should not be null");
        assertEquals(0, score.getScore(), "Initial score should be 0");
        assertEquals(0, score.totalLinesProperty().get(), "Initial lines should be 0");
        assertEquals(1, score.levelProperty().get(), "Initial level should be 1");
    }

    @Test
    void testCreateNewBrick() {
        boolean gameOver = board.createNewBrick();
        assertFalse(gameOver, "Game should not be over initially");
        
        ViewData viewData = board.getViewData();
        assertNotNull(viewData, "View data should not be null");
        assertNotNull(viewData.getBrickData(), "Brick data should not be null");
        assertTrue(viewData.getxPosition() >= 0, "X position should be valid");
        assertTrue(viewData.getyPosition() >= 0, "Y position should be valid");
    }

    @Test
    void testBasicMovement() {
        board.createNewBrick();
        ViewData initialViewData = board.getViewData();
        
        // Test movement down
        boolean moved = board.moveBrickDown();
        assertTrue(moved, "Should be able to move down initially");
        
        ViewData newViewData = board.getViewData();
        assertEquals(initialViewData.getyPosition() + 1, newViewData.getyPosition(), 
            "Y position should increase by 1");
        
        // Test movement left
        int initialX = newViewData.getxPosition();
        moved = board.moveBrickLeft();
        assertTrue(moved, "Should be able to move left");
        assertEquals(initialX - 1, board.getViewData().getxPosition(), 
            "X position should decrease by 1");
        
        // Test movement right
        initialX = board.getViewData().getxPosition();
        moved = board.moveBrickRight();
        assertTrue(moved, "Should be able to move right");
        assertEquals(initialX + 1, board.getViewData().getxPosition(), 
            "X position should increase by 1");
    }

    @Test
    void testRotation() {
        board.createNewBrick();
        ViewData initialViewData = board.getViewData();
        int[][] initialShape = initialViewData.getBrickData();
        
        boolean rotated = board.rotateLeftBrick();
        assertTrue(rotated, "Should be able to rotate initially");
        
        ViewData newViewData = board.getViewData();
        // Position should not change during rotation
        assertEquals(initialViewData.getxPosition(), newViewData.getxPosition());
        assertEquals(initialViewData.getyPosition(), newViewData.getyPosition());
    }

    @Test
    void testMergeBrickToBackground() {
        board.createNewBrick();
        ViewData viewData = board.getViewData();
        
        int[][] initialMatrix = board.getBoardMatrix();
        int[][] brickData = viewData.getBrickData();
        int x = viewData.getxPosition();
        int y = viewData.getyPosition();
        
        board.mergeBrickToBackground();
        
        int[][] newMatrix = board.getBoardMatrix();
        
        // Check that at least one brick was merged to background
        boolean brickMerged = false;
        for (int row = 0; row < brickData.length; row++) {
            for (int col = 0; col < brickData[row].length; col++) {
                if (brickData[row][col] != 0) {
                    int boardY = y + row;
                    int boardX = x + col;
                    if (boardY >= 0 && boardY < newMatrix.length && 
                        boardX >= 0 && boardX < newMatrix[boardY].length) {
                        if (newMatrix[boardY][boardX] == brickData[row][col]) {
                            brickMerged = true;
                            break;
                        }
                    }
                }
            }
            if (brickMerged) break;
        }
        assertTrue(brickMerged, "Brick should be merged to background");
    }

    @Test
    void testClearRows() {
        // Fill bottom row completely
        int[][] matrix = board.getBoardMatrix();
        for (int col = 0; col < matrix[0].length; col++) {
            matrix[matrix.length - 1][col] = 1;
        }
        
        ClearRow clearRow = board.clearRows();
        
        assertEquals(1, clearRow.getLinesRemoved(), "One line should be removed");
        assertEquals(50, clearRow.getScoreBonus(), "Score bonus should be 50 for 1 line");
        assertNotNull(clearRow.getNewMatrix(), "New matrix should be returned");
    }

    @Test
    void testHoldBrick() {
        board.createNewBrick();
        ViewData initialViewData = board.getViewData();
        int[][] initialBrickData = initialViewData.getBrickData();
        
        ViewData holdViewData = board.holdBrick();
        
        assertNotNull(holdViewData, "Hold should return view data");
        assertNotNull(holdViewData.getHoldBrickData(), "Hold brick data should not be null");
        assertArrayEquals(initialBrickData, holdViewData.getHoldBrickData(), 
            "Hold brick should match current brick");
    }

    @Test
    void testNewGame() {
        // Play a bit to modify the board
        board.createNewBrick();
        board.moveBrickDown();
        board.getScore().add(100);
        board.getScore().addLines(5);
        
        // Reset game
        board.newGame();
        
        // Check that everything is reset
        int[][] matrix = board.getBoardMatrix();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                assertEquals(0, matrix[row][col], "Board should be empty after new game");
            }
        }
        
        assertEquals(0, board.getScore().getScore(), "Score should be reset");
        assertEquals(0, board.getScore().totalLinesProperty().get(), "Lines should be reset");
        assertEquals(1, board.getScore().levelProperty().get(), "Level should be reset");
    }

    @Test
    void testCustomBoardDimensions() {
        SimpleBoard customBoard = new SimpleBoard(5, 15);
        int[][] matrix = customBoard.getBoardMatrix();
        
        assertEquals(15, matrix.length, "Custom board should have 15 rows");
        assertEquals(5, matrix[0].length, "Custom board should have 5 columns");
    }
}

package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private SimpleBoard board;
    private MockBrickGenerator mockGenerator;

    private static class MockBrickGenerator implements BrickGenerator {
        private final List<Brick> queue = new ArrayList<>();
        private int nextBrickIndex = 0;

        public void addBrick(Brick brick) {
            queue.add(brick);
        }

        @Override
        public Brick getBrick() {
            if (queue.isEmpty()) {
                return createMockBrick(new int[][]{{1}});
            }
            return queue.get(nextBrickIndex++ % queue.size());
        }

        @Override
        public Brick getNextBrick() {
            if (queue.isEmpty()) {
                return createMockBrick(new int[][]{{1}});
            }
            return queue.get(nextBrickIndex % queue.size());
        }
    }

    private static Brick createMockBrick(int[][] shape) {
        return () -> Collections.singletonList(shape);
    }

    @BeforeEach
    void setUp() {
        mockGenerator = new MockBrickGenerator();
        Brick dot = createMockBrick(new int[][]{{1}});
        mockGenerator.addBrick(dot);

        board = new SimpleBoard(10, 20, mockGenerator);
        board.createNewBrick();
    }

    @Test
    void testMoveDownStopsAtBottom() {
        int moves = 0;
        while (board.moveBrickDown()) {
            moves++;
        }
        assertEquals(18, moves, "Should move 18 times to reach bottom");
    }

    @Test
    void testMoveDownStopsAboveAnotherBrick() {
        while (board.moveBrickDown()) {}
        board.mergeBrickToBackground();

        board.createNewBrick();

        int moves = 0;
        while (board.moveBrickDown()) {
            moves++;
        }
        assertEquals(17, moves, "Should stop 1 row above existing brick");
    }

    @Test
    void testNewGameResetsBoardScoreAndState() {
        board.getScore().add(100);
        while (board.moveBrickDown()) {}
        board.mergeBrickToBackground();

        board.newGame();

        assertEquals(0, board.getScore().getScore(), "Score should be 0");
        int[][] matrix = board.getBoardMatrix();
        assertEquals(0, matrix[19][4], "Board should be cleared");
    }

    @Test
    void testHardDropPlacesBrickAtLowestPosition() {
        int drops = 0;
        while (board.moveBrickDown()) {
            drops++;
        }
        assertEquals(18, drops, "Hard drop simulation: 18 steps to bottom");
    }

    @Test
    void testLineClearWithMatrixOperations() {
        int[][] fullRow = new int[5][5];
        for (int c = 0; c < 5; c++) {
            fullRow[4][c] = 1;
        }
        
        ClearRow result = com.comp2042.logic.MatrixOperations.checkRemoving(fullRow);
        assertEquals(1, result.getLinesRemoved(), "Should clear 1 line");
        assertEquals(50, result.getScoreBonus(), "1 line = 50 points");
    }

    @Test
    void testMultipleLineClearWithMatrixOperations() {
        int[][] fullRows = new int[5][5];
        for (int c = 0; c < 5; c++) {
            fullRows[3][c] = 1;
            fullRows[4][c] = 1;
        }
        
        ClearRow result = com.comp2042.logic.MatrixOperations.checkRemoving(fullRows);
        assertEquals(2, result.getLinesRemoved(), "Should clear 2 lines");
        assertEquals(200, result.getScoreBonus(), "2 lines = 200 points");
    }

    @Test
    void testSpawnBrickFailsOnBlockedTop() {
        int[][] tallShape = new int[19][1];
        for (int k = 0; k < 19; k++) {
            tallShape[k][0] = 1;
        }

        MockBrickGenerator tallGen = new MockBrickGenerator();
        tallGen.addBrick(createMockBrick(tallShape));
        tallGen.addBrick(createMockBrick(new int[][]{{1}}));

        SimpleBoard tallBoard = new SimpleBoard(10, 20, tallGen);
        tallBoard.createNewBrick();

        while (tallBoard.moveBrickDown()) {}
        tallBoard.mergeBrickToBackground();

        boolean conflict = tallBoard.createNewBrick();
        assertTrue(conflict, "Should return true indicating Game Over");
    }

    @Test
    void testScoreBonusCalculation() {
        ClearRow one = new ClearRow(1, new int[5][5], 50);
        assertEquals(50, one.getScoreBonus());
        
        ClearRow two = new ClearRow(2, new int[5][5], 200);
        assertEquals(200, two.getScoreBonus());
        
        ClearRow four = new ClearRow(4, new int[5][5], 800);
        assertEquals(800, four.getScoreBonus());
    }

    @Test
    void testMoveBrickLeft() {
        int initialX = board.getViewData().getxPosition();
        assertTrue(board.moveBrickLeft());
        assertEquals(initialX - 1, board.getViewData().getxPosition());
    }

    @Test
    void testMoveBrickRight() {
        int initialX = board.getViewData().getxPosition();
        assertTrue(board.moveBrickRight());
        assertEquals(initialX + 1, board.getViewData().getxPosition());
    }
}

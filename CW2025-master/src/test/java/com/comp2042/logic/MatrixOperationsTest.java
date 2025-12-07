package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    // --- intersect tests (collision detection) ---

    @Test
    void testIntersectNoCollisionEmptyBoard() {
        int[][] board = new int[10][10]; // Empty 10x10 board
        int[][] brick = { { 1 } }; // 1x1 brick

        // Place at center
        assertFalse(MatrixOperations.intersect(board, brick, 5, 5),
                "Should not collide on empty board");
    }

    @Test
    void testIntersectCollisionWithExistingBlock() {
        int[][] board = new int[10][10];
        board[5][5] = 1; // Existing block at (5,5)

        int[][] brick = { { 1 } }; // 1x1 brick

        // Place brick at same position
        assertTrue(MatrixOperations.intersect(board, brick, 5, 5),
                "Should collide with existing block");
    }

    @Test
    void testIntersectCollisionOutOfBoundsLeft() {
        int[][] board = new int[10][10];
        int[][] brick = { { 1 } };

        // Place at x = -1 (out of bounds left)
        assertTrue(MatrixOperations.intersect(board, brick, -1, 5),
                "Should collide when out of bounds (left)");
    }

    @Test
    void testIntersectCollisionOutOfBoundsRight() {
        int[][] board = new int[10][10];
        int[][] brick = { { 1 } };

        // Place at x = 10 (out of bounds right, board width is 10 -> max index 9)
        assertTrue(MatrixOperations.intersect(board, brick, 10, 5),
                "Should collide when out of bounds (right)");
    }

    @Test
    void testIntersectCollisionOutOfBoundsTop() {
        int[][] board = new int[10][10];
        int[][] brick = { { 1 } };

        // Place at y = -1 (out of bounds top)
        assertTrue(MatrixOperations.intersect(board, brick, 5, -1),
                "Should collide when out of bounds (top)");
    }

    @Test
    void testIntersectCollisionOutOfBoundsBottom() {
        int[][] board = new int[10][10];
        int[][] brick = { { 1 } };

        // Place at y = 10 (out of bounds bottom)
        assertTrue(MatrixOperations.intersect(board, brick, 5, 10),
                "Should collide when out of bounds (bottom)");
    }

    @Test
    void testIntersectLargeBrickPartiallyOutOfBounds() {
        int[][] board = new int[10][10];
        // 2x2 brick
        int[][] brick = {
                { 1, 1 },
                { 1, 1 }
        };

        // Place at (9, 5) - brick extends to x=10, which is OOB
        assertTrue(MatrixOperations.intersect(board, brick, 9, 5),
                "2x2 brick at x=9 should be out of bounds");
    }

    @Test
    void testIntersectLargeBrickFullyInBounds() {
        int[][] board = new int[10][10];
        int[][] brick = {
                { 1, 1 },
                { 1, 1 }
        };

        // Place at (8, 5) - brick occupies (8,5), (9,5), (8,6), (9,6) - all valid
        assertFalse(MatrixOperations.intersect(board, brick, 8, 5),
                "2x2 brick at x=8 should fit within bounds");
    }

    @Test
    void testIntersectBrickWithHoles() {
        int[][] board = new int[10][10];
        board[5][5] = 1; // Existing block

        // L-shaped brick with hole
        int[][] brick = {
                { 1, 0 },
                { 1, 1 }
        };

        // Place so that the "hole" (0) is over the existing block
        // Brick at (4, 5): brick[0][0]=1 at (4,5), brick[0][1]=0 at (5,5),
        // brick[1][0]=1 at (4,6), brick[1][1]=1 at (5,6)
        // The "0" at brick[0][1] lands on (5,5) where there's a block, but 0s don't
        // collide
        assertFalse(MatrixOperations.intersect(board, brick, 4, 5),
                "Hole in brick should not collide with existing block");
    }

    @Test
    void testIntersectBrickCollidesWithFilledPart() {
        int[][] board = new int[10][10];
        board[5][4] = 1; // Block at (4,5) in x,y (which is board[5][4])

        // L-shaped brick
        int[][] brick = {
                { 1, 0 },
                { 1, 1 }
        };

        // Place at (4, 5): brick[0][0]=1 at (4,5) -> collides with board[5][4]
        assertTrue(MatrixOperations.intersect(board, brick, 4, 5),
                "Filled part of brick should collide with existing block");
    }

    // --- copy tests ---

    @Test
    void testCopyCreatesIndependentMatrix() {
        int[][] original = {
                { 1, 2 },
                { 3, 4 }
        };

        int[][] copied = MatrixOperations.copy(original);

        // Modify copied
        copied[0][0] = 99;

        // Original should be unchanged
        assertEquals(1, original[0][0], "Original should not be affected by copy modification");
    }

    // --- merge tests ---

    @Test
    void testMergePlacesBrickOnBoard() {
        int[][] board = new int[5][5];
        int[][] brick = { { 1 } };

        int[][] merged = MatrixOperations.merge(board, brick, 2, 2);

        assertEquals(1, merged[2][2], "Brick should be placed at (2,2)");
        assertEquals(0, board[2][2], "Original board should be unchanged");
    }

    @Test
    void testMergePreservesExistingBlocks() {
        int[][] board = new int[5][5];
        board[0][0] = 5;

        int[][] brick = { { 1 } };
        int[][] merged = MatrixOperations.merge(board, brick, 2, 2);

        assertEquals(5, merged[0][0], "Existing block should be preserved");
        assertEquals(1, merged[2][2], "New brick should be added");
    }

    // --- checkRemoving tests ---

    @Test
    void testCheckRemovingNoFullRows() {
        int[][] board = new int[5][5];
        board[4][0] = 1; // Partial row

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved(), "No lines should be removed");
        assertEquals(0, result.getScoreBonus(), "Score bonus should be 0");
    }

    @Test
    void testCheckRemovingSingleFullRow() {
        int[][] board = new int[5][5];
        // Fill bottom row
        for (int c = 0; c < 5; c++) {
            board[4][c] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(1, result.getLinesRemoved(), "One line should be removed");
        assertEquals(50, result.getScoreBonus(), "Score bonus for 1 line = 50*1*1 = 50");
    }

    @Test
    void testCheckRemovingMultipleFullRows() {
        int[][] board = new int[5][5];
        // Fill bottom 2 rows
        for (int c = 0; c < 5; c++) {
            board[4][c] = 1;
            board[3][c] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(2, result.getLinesRemoved(), "Two lines should be removed");
        assertEquals(200, result.getScoreBonus(), "Score bonus for 2 lines = 50*2*2 = 200");
    }

    @Test
    void testCheckRemovingRowsShiftDown() {
        int[][] board = new int[5][5];
        // Fill bottom row completely
        for (int c = 0; c < 5; c++) {
            board[4][c] = 1;
        }
        // Put a block in row 3
        board[3][2] = 2;

        ClearRow result = MatrixOperations.checkRemoving(board);
        int[][] newMatrix = result.getNewMatrix();

        // After clearing bottom row, the block that was at row 3 should now be at row 4
        assertEquals(2, newMatrix[4][2], "Block should shift down after row clear");
        assertEquals(0, newMatrix[3][2], "Original row should be empty");
    }
}

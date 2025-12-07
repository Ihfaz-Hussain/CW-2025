package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void testConstructorAndGetters() {
        int[][] matrix = { { 1, 2 }, { 3, 4 } };
        ClearRow clearRow = new ClearRow(2, matrix, 200);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(200, clearRow.getScoreBonus());
    }

    @Test
    void testGetNewMatrixReturnsCopy() {
        int[][] matrix = { { 1, 2 }, { 3, 4 } };
        ClearRow clearRow = new ClearRow(1, matrix, 50);

        int[][] returned = clearRow.getNewMatrix();

        // Modify returned matrix
        returned[0][0] = 99;

        // Get again and verify original is unchanged
        int[][] returnedAgain = clearRow.getNewMatrix();
        assertEquals(1, returnedAgain[0][0], "getNewMatrix should return a copy");
    }

    @Test
    void testZeroLinesRemoved() {
        int[][] matrix = new int[5][5];
        ClearRow clearRow = new ClearRow(0, matrix, 0);

        assertEquals(0, clearRow.getLinesRemoved());
        assertEquals(0, clearRow.getScoreBonus());
    }

    @Test
    void testScoreBonusForMultipleLines() {
        // Based on formula: 50 * lines * lines
        // 1 line = 50, 2 lines = 200, 3 lines = 450, 4 lines = 800
        int[][] matrix = new int[5][5];

        ClearRow one = new ClearRow(1, matrix, 50);
        assertEquals(50, one.getScoreBonus());

        ClearRow two = new ClearRow(2, matrix, 200);
        assertEquals(200, two.getScoreBonus());

        ClearRow four = new ClearRow(4, matrix, 800);
        assertEquals(800, four.getScoreBonus());
    }
}

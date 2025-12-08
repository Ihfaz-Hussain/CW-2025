package com.comp2042.logic;

import com.comp2042.model.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing matrix operations for Tetris game logic.
 * Includes methods for collision detection, matrix copying, and row clearing.
 * This class cannot be instantiated as all methods are static.
 */
public class MatrixOperations {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MatrixOperations(){

    }

    /**
     * Checks if a brick placed at the specified coordinates would intersect
     * with the game board matrix or go out of bounds.
     *
     * @param matrix the game board matrix to check against
     * @param brick  the brick shape matrix to test
     * @param x      the x (column) position to place the brick
     * @param y      the y (row) position to place the brick
     * @return {@code true} if there is a collision or out of bounds, {@code false} otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                int targetX = x + col;
                int targetY = y + row;
                if (brick[row][col] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the specified coordinates are out of bounds for the given matrix.
     *
     * @param matrix the matrix to check bounds against
     * @param targetX the x coordinate to test
     * @param targetY the y coordinate to test
     * @return {@code true} if coordinates are out of bounds, {@code false} otherwise
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * Both the outer array and all inner arrays are copied.
     *
     * @param original the matrix to copy
     * @return a deep copy of the original matrix
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                int targetX = x + col;
                int targetY = y + row;
                if (brick[row][col] != 0) {
                    copy[targetY][targetX] = brick[row][col];
                }
            }
        }
        return copy;
    }


    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

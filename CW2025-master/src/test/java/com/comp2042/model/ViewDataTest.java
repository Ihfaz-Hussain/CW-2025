package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void testConstructorAndPositionGetters() {
        int[][] brick = { { 1 } };
        int[][] next = { { 2 } };
        int[][] hold = { { 3 } };

        ViewData viewData = new ViewData(brick, 5, 10, next, hold);

        assertEquals(5, viewData.getxPosition());
        assertEquals(10, viewData.getyPosition());
    }

    @Test
    void testGetBrickDataReturnsCopy() {
        int[][] brick = { { 1, 2 }, { 3, 4 } };
        ViewData viewData = new ViewData(brick, 0, 0, null, null);

        int[][] returned = viewData.getBrickData();
        returned[0][0] = 99;

        int[][] returnedAgain = viewData.getBrickData();
        assertEquals(1, returnedAgain[0][0], "getBrickData should return a copy");
    }

    @Test
    void testGetNextBrickDataReturnsCopy() {
        int[][] next = { { 5, 6 } };
        ViewData viewData = new ViewData(new int[][] { { 1 } }, 0, 0, next, null);

        int[][] returned = viewData.getNextBrickData();
        returned[0][0] = 99;

        int[][] returnedAgain = viewData.getNextBrickData();
        assertEquals(5, returnedAgain[0][0], "getNextBrickData should return a copy");
    }

    @Test
    void testGetHoldBrickDataReturnsCopy() {
        int[][] hold = { { 7, 8 } };
        ViewData viewData = new ViewData(new int[][] { { 1 } }, 0, 0, new int[][] { { 2 } }, hold);

        int[][] returned = viewData.getHoldBrickData();
        returned[0][0] = 99;

        int[][] returnedAgain = viewData.getHoldBrickData();
        assertEquals(7, returnedAgain[0][0], "getHoldBrickData should return a copy");
    }

    @Test
    void testGetHoldBrickDataNullReturnsNull() {
        ViewData viewData = new ViewData(new int[][] { { 1 } }, 0, 0, new int[][] { { 2 } }, null);

        assertNull(viewData.getHoldBrickData(), "Should return null when hold is null");
    }

    @Test
    void testBrickDataPreserved() {
        int[][] brick = { { 1, 0 }, { 1, 1 } };
        ViewData viewData = new ViewData(brick, 3, 7, new int[][] { { 2 } }, null);

        int[][] data = viewData.getBrickData();
        assertArrayEquals(new int[] { 1, 0 }, data[0]);
        assertArrayEquals(new int[] { 1, 1 }, data[1]);
    }
}

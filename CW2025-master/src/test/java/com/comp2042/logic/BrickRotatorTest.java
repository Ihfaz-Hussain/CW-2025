package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
    }

    // Helper to create mock bricks
    private Brick createBrick(int[][]... shapes) {
        List<int[][]> shapeList = Arrays.asList(shapes);
        return () -> shapeList;
    }

    @Test
    void testSetBrickResetsCurrentShape() {
        int[][] shape1 = { { 1 } };
        int[][] shape2 = { { 2 } };
        Brick brick = createBrick(shape1, shape2);

        rotator.setBrick(brick);

        assertArrayEquals(shape1, rotator.getCurrentShape(), "Should start at shape 0");
    }

    @Test
    void testGetCurrentShapeReturnsCorrectShape() {
        int[][] shape = { { 1, 2 }, { 3, 4 } };
        Brick brick = createBrick(shape);

        rotator.setBrick(brick);

        assertArrayEquals(shape, rotator.getCurrentShape());
    }

    @Test
    void testGetNextShapeReturnsNextRotation() {
        int[][] shape0 = { { 1 } };
        int[][] shape1 = { { 2 } };
        int[][] shape2 = { { 3 } };
        Brick brick = createBrick(shape0, shape1, shape2);

        rotator.setBrick(brick);

        NextShapeInfo next = rotator.getNextShape();
        assertArrayEquals(shape1, next.getShape(), "Next shape should be shape1");
        assertEquals(1, next.getPosition(), "Next position should be 1");
    }

    @Test
    void testGetNextShapeWrapsAround() {
        int[][] shape0 = { { 1 } };
        int[][] shape1 = { { 2 } };
        Brick brick = createBrick(shape0, shape1);

        rotator.setBrick(brick);
        rotator.setCurrentShape(1); // At last shape

        NextShapeInfo next = rotator.getNextShape();
        assertArrayEquals(shape0, next.getShape(), "Should wrap to shape0");
        assertEquals(0, next.getPosition(), "Position should wrap to 0");
    }

    @Test
    void testSetCurrentShapeChangesCurrentShape() {
        int[][] shape0 = { { 1 } };
        int[][] shape1 = { { 2 } };
        int[][] shape2 = { { 3 } };
        Brick brick = createBrick(shape0, shape1, shape2);

        rotator.setBrick(brick);
        rotator.setCurrentShape(2);

        assertArrayEquals(shape2, rotator.getCurrentShape());
    }

    @Test
    void testGetBrickReturnsSameBrick() {
        int[][] shape = { { 1 } };
        Brick brick = createBrick(shape);

        rotator.setBrick(brick);

        assertSame(brick, rotator.getBrick());
    }

    @Test
    void testSingleShapeBrickNextShapeReturnsSame() {
        int[][] shape = { { 1 } };
        Brick brick = createBrick(shape);

        rotator.setBrick(brick);

        NextShapeInfo next = rotator.getNextShape();
        assertArrayEquals(shape, next.getShape(), "Single shape brick should return same shape");
        assertEquals(0, next.getPosition(), "Position should be 0");
    }
}

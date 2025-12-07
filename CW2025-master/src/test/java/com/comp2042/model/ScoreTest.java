package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    // Test initial state
    @Test
    void testInitialScoreIsZero() {
        assertEquals(0, score.getScore(), "Initial score should be 0");
    }

    @Test
    void testInitialLevelIsOne() {
        assertEquals(1, score.levelProperty().get(), "Initial level should be 1");
    }

    @Test
    void testInitialLinesIsZero() {
        assertEquals(0, score.totalLinesProperty().get(), "Initial lines should be 0");
    }

    // Test adding score
    @Test
    void testAddScore() {
        score.add(100);
        assertEquals(100, score.getScore());

        score.add(50);
        assertEquals(150, score.getScore());
    }

    @Test
    void testAddNegativeScore() {
        score.add(-10);
        assertEquals(-10, score.getScore(), "Negative scores should be allowed");
    }

    // Test adding lines and level progression
    @Test
    void testAddLinesUpdatesTotal() {
        score.addLines(2);
        assertEquals(2, score.totalLinesProperty().get());

        score.addLines(3);
        assertEquals(5, score.totalLinesProperty().get());
    }

    @Test
    void testAddLinesZeroDoesNothing() {
        score.addLines(0);
        assertEquals(0, score.totalLinesProperty().get());
    }

    @Test
    void testAddLinesNegativeDoesNothing() {
        score.addLines(5);
        score.addLines(-2);
        assertEquals(5, score.totalLinesProperty().get(), "Negative lines should be ignored");
    }

    @Test
    void testLevelUpEvery5Lines() {
        // Level = 1 + (totalLines / 5)
        score.addLines(4);
        assertEquals(1, score.levelProperty().get(), "4 lines = level 1");

        score.addLines(1); // Total: 5
        assertEquals(2, score.levelProperty().get(), "5 lines = level 2");

        score.addLines(5); // Total: 10
        assertEquals(3, score.levelProperty().get(), "10 lines = level 3");
    }

    // Test reset
    @Test
    void testResetClearsEverything() {
        score.add(500);
        score.addLines(15);

        score.reset();

        assertEquals(0, score.getScore(), "Score should be 0 after reset");
        assertEquals(0, score.totalLinesProperty().get(), "Lines should be 0 after reset");
        assertEquals(1, score.levelProperty().get(), "Level should be 1 after reset");
    }

    // Test property bindings
    @Test
    void testScorePropertyUpdates() {
        score.add(42);
        assertEquals(42, score.scoreProperty().get());
    }
}

package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void testRunningState() {
        GameState state = GameState.RUNNING;
        assertEquals("RUNNING", state.name());
    }

    @Test
    void testPausedState() {
        GameState state = GameState.PAUSED;
        assertEquals("PAUSED", state.name());
    }

    @Test
    void testOverState() {
        GameState state = GameState.OVER;
        assertEquals("OVER", state.name());
    }

    @Test
    void testEnumValuesCount() {
        GameState[] values = GameState.values();
        assertEquals(3, values.length, "Should have exactly 3 game states");
    }

    @Test
    void testValueOf() {
        assertEquals(GameState.RUNNING, GameState.valueOf("RUNNING"));
        assertEquals(GameState.PAUSED, GameState.valueOf("PAUSED"));
        assertEquals(GameState.OVER, GameState.valueOf("OVER"));
    }

    @Test
    void testEnumOrdinals() {
        assertEquals(0, GameState.RUNNING.ordinal());
        assertEquals(1, GameState.PAUSED.ordinal());
        assertEquals(2, GameState.OVER.ordinal());
    }
}

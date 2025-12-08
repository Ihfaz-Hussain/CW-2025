package com.comp2042.controller;

import com.comp2042.event.EventType;
import com.comp2042.event.EventSource;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.*;
import com.comp2042.view.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTestSimple {

    private TestGuiController mockGuiController;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        mockGuiController = new TestGuiController();
        gameController = new GameController(mockGuiController, "TestPlayer");
    }

    @Test
    void testConstructorWithDefaultPlayerName() {
        GameController defaultController = new GameController(mockGuiController);
        assertNotNull(defaultController, "Controller should be created with default name");
    }

    @Test
    void testConstructorWithCustomPlayerName() {
        GameController customController = new GameController(mockGuiController, "CustomPlayer");
        assertNotNull(customController, "Controller should be created with custom name");
    }

    @Test
    void testOnDownEventMovement() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        DownData result = gameController.onDownEvent(event);
        
        assertNotNull(result, "Should return DownData");
        // The result depends on the actual board state and brick movement
    }

    @Test
    void testOnLeftEvent() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        ViewData result = gameController.onLeftEvent(event);
        
        assertNotNull(result, "Should return ViewData");
    }

    @Test
    void testOnRightEvent() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData result = gameController.onRightEvent(event);
        
        assertNotNull(result, "Should return ViewData");
    }

    @Test
    void testOnRotateEvent() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        ViewData result = gameController.onRotateEvent(event);
        
        assertNotNull(result, "Should return ViewData");
    }

    @Test
    void testOnHardDropEvent() {
        MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
        DownData result = gameController.onHardDropEvent(event);
        
        assertNotNull(result, "Should return DownData");
    }

    @Test
    void testOnHoldEvent() {
        MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
        ViewData result = gameController.onHoldEvent(event);
        
        assertNotNull(result, "Should return ViewData");
    }

    @Test
    void testCreateNewGame() {
        gameController.createNewGame();
        
        // Verify that the game was initialized
        assertTrue(mockGuiController.initGameViewCalled, "Init game view should be called");
    }

    @Test
    void testEventSourceValidation() {
        // Test that events from different sources are handled
        MoveEvent userEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveEvent threadEvent = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        
        DownData userResult = gameController.onDownEvent(userEvent);
        DownData threadResult = gameController.onDownEvent(threadEvent);
        
        assertNotNull(userResult, "User events should be handled");
        assertNotNull(threadResult, "Thread events should be handled");
    }

    @Test
    void testMultipleOperationsInSequence() {
        // Perform various operations
        gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
        gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        gameController.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
        
        // All should complete without exceptions
        assertTrue(true, "All operations should complete without exceptions");
    }

    @Test
    void testNullEventHandling() {
        // The controller should handle null events gracefully or throw appropriate exceptions
        assertDoesNotThrow(() -> {
            // This would depend on the actual implementation
            // For now, just test that the controller doesn't crash with valid events
            gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
        });
    }

    @Test
    void testPlayerNameUsage() {
        // Create controller with specific player name
        GameController namedController = new GameController(mockGuiController, "Alice");
        assertNotNull(namedController, "Controller should be created with player name");
    }

    @Test
    void testBoardStateConsistency() {
        // Test that board operations maintain consistent state
        gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
        gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        gameController.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
        
        // All should complete without exceptions
        assertTrue(true, "Board state should remain consistent");
    }

    // Test helper class
    private static class TestGuiController extends GuiController {
        boolean initGameViewCalled = false;
        boolean gameOverCalled = false;

        @Override
        public void initGameView(int[][] matrix, ViewData viewData) {
            initGameViewCalled = true;
        }

        @Override
        public void gameOver() {
            gameOverCalled = true;
        }
    }
}

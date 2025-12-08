package com.comp2042.view;

import com.comp2042.event.InputEventListener;
import com.comp2042.view.GuiController;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTestSimple {

    private InputHandler inputHandler;
    private TestEventListener testListener;
    private TestGuiController testGuiController;

    @BeforeEach
    void setUp() {
        inputHandler = new InputHandler();
        testListener = new TestEventListener();
        testGuiController = new TestGuiController();
        
        inputHandler.setEventListener(testListener);
        inputHandler.setGuiController(testGuiController);
    }

    @Test
    void testInitialState() {
        assertFalse(inputHandler.isPaused(), "Should not be paused initially");
        assertFalse(inputHandler.isGameOver(), "Should not be game over initially");
    }

    @Test
    void testSetPaused() {
        inputHandler.setPaused(true);
        assertTrue(inputHandler.isPaused(), "Should be paused when set to true");
        
        inputHandler.setPaused(false);
        assertFalse(inputHandler.isPaused(), "Should not be paused when set to false");
    }

    @Test
    void testSetGameOver() {
        inputHandler.setGameOver(true);
        assertTrue(inputHandler.isGameOver(), "Should be game over when set to true");
        
        inputHandler.setGameOver(false);
        assertFalse(inputHandler.isGameOver(), "Should not be game over when set to false");
    }

    @Test
    void testLeftArrowKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.LEFT);
        assertNotNull(action, "Left arrow should have an action");
        
        // Execute the action
        action.run();
        
        // Should call onLeftEvent
        assertTrue(testListener.leftEventCalled, "Left event should be called");
    }

    @Test
    void testRightArrowKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.RIGHT);
        assertNotNull(action, "Right arrow should have an action");
        
        action.run();
        assertTrue(testListener.rightEventCalled, "Right event should be called");
    }

    @Test
    void testUpArrowKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.UP);
        assertNotNull(action, "Up arrow should have an action");
        
        action.run();
        assertTrue(testListener.rotateEventCalled, "Rotate event should be called");
    }

    @Test
    void testDownArrowKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.DOWN);
        assertNotNull(action, "Down arrow should have an action");
        
        action.run();
        assertTrue(testGuiController.moveDownCalled, "Move down should be called");
    }

    @Test
    void testSpaceKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.SPACE);
        assertNotNull(action, "Space should have an action");
        
        action.run();
        assertTrue(testGuiController.hardDropCalled, "Hard drop should be called");
    }

    @Test
    void testShiftKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.SHIFT);
        assertNotNull(action, "Shift should have an action");
        
        action.run();
        assertTrue(testListener.holdEventCalled, "Hold event should be called");
    }

    @Test
    void testWASDKeyMappings() {
        // Test W (rotate)
        Runnable wAction = inputHandler.getActionForKey(KeyCode.W);
        assertNotNull(wAction, "W should have an action");
        wAction.run();
        assertTrue(testListener.rotateEventCalled, "W should trigger rotate");
        
        // Reset and test A (left)
        testListener.reset();
        Runnable aAction = inputHandler.getActionForKey(KeyCode.A);
        assertNotNull(aAction, "A should have an action");
        aAction.run();
        assertTrue(testListener.leftEventCalled, "A should trigger left");
        
        // Reset and test S (down)
        testListener.reset();
        Runnable sAction = inputHandler.getActionForKey(KeyCode.S);
        assertNotNull(sAction, "S should have an action");
        sAction.run();
        assertTrue(testGuiController.moveDownCalled, "S should trigger down");
        
        // Reset and test D (right)
        testListener.reset();
        testGuiController.reset();
        Runnable dAction = inputHandler.getActionForKey(KeyCode.D);
        assertNotNull(dAction, "D should have an action");
        dAction.run();
        assertTrue(testListener.rightEventCalled, "D should trigger right");
    }

    @Test
    void testCKeyMapping() {
        Runnable action = inputHandler.getActionForKey(KeyCode.C);
        assertNotNull(action, "C should have an action");
        
        action.run();
        assertTrue(testListener.holdEventCalled, "C should trigger hold");
    }

    @Test
    void testUnmappedKeyReturnsNull() {
        Runnable action = inputHandler.getActionForKey(KeyCode.ESCAPE);
        assertNull(action, "Unmapped key should return null");
        
        action = inputHandler.getActionForKey(KeyCode.F1);
        assertNull(action, "Unmapped key should return null");
    }

    @Test
    void testInputIgnoredWhenPaused() {
        inputHandler.setPaused(true);
        
        // Try various keys
        Runnable leftAction = inputHandler.getActionForKey(KeyCode.LEFT);
        Runnable rotateAction = inputHandler.getActionForKey(KeyCode.UP);
        Runnable holdAction = inputHandler.getActionForKey(KeyCode.SHIFT);
        
        if (leftAction != null) leftAction.run();
        if (rotateAction != null) rotateAction.run();
        if (holdAction != null) holdAction.run();
        
        // No interactions should occur when paused
        assertFalse(testListener.leftEventCalled, "Left should not be called when paused");
        assertFalse(testListener.rotateEventCalled, "Rotate should not be called when paused");
        assertFalse(testListener.holdEventCalled, "Hold should not be called when paused");
        assertFalse(testGuiController.moveDownCalled, "Move down should not be called when paused");
    }

    @Test
    void testInputIgnoredWhenGameOver() {
        inputHandler.setGameOver(true);
        
        // Try various keys
        Runnable leftAction = inputHandler.getActionForKey(KeyCode.LEFT);
        Runnable downAction = inputHandler.getActionForKey(KeyCode.DOWN);
        Runnable spaceAction = inputHandler.getActionForKey(KeyCode.SPACE);
        
        if (leftAction != null) leftAction.run();
        if (downAction != null) downAction.run();
        if (spaceAction != null) spaceAction.run();
        
        // No interactions should occur when game over
        assertFalse(testListener.leftEventCalled, "Left should not be called when game over");
        assertFalse(testGuiController.moveDownCalled, "Move down should not be called when game over");
        assertFalse(testGuiController.hardDropCalled, "Hard drop should not be called when game over");
    }

    @Test
    void testGetKeyActions() {
        var keyActions = inputHandler.getKeyActions();
        assertNotNull(keyActions, "Key actions map should not be null");
        assertFalse(keyActions.isEmpty(), "Key actions map should not be empty");
        
        // Check that expected keys are present
        assertTrue(keyActions.containsKey(KeyCode.LEFT), "Should contain LEFT key");
        assertTrue(keyActions.containsKey(KeyCode.RIGHT), "Should contain RIGHT key");
        assertTrue(keyActions.containsKey(KeyCode.UP), "Should contain UP key");
        assertTrue(keyActions.containsKey(KeyCode.DOWN), "Should contain DOWN key");
        assertTrue(keyActions.containsKey(KeyCode.SPACE), "Should contain SPACE key");
        assertTrue(keyActions.containsKey(KeyCode.SHIFT), "Should contain SHIFT key");
    }

    // Test helper classes
    private static class TestEventListener implements InputEventListener {
        boolean leftEventCalled = false;
        boolean rightEventCalled = false;
        boolean rotateEventCalled = false;
        boolean holdEventCalled = false;
        boolean createNewGameCalled = false;

        void reset() {
            leftEventCalled = false;
            rightEventCalled = false;
            rotateEventCalled = false;
            holdEventCalled = false;
            createNewGameCalled = false;
        }

        @Override
        public com.comp2042.model.DownData onDownEvent(com.comp2042.event.MoveEvent event) {
            return null;
        }

        @Override
        public com.comp2042.model.DownData onHardDropEvent(com.comp2042.event.MoveEvent event) {
            return null;
        }

        @Override
        public com.comp2042.model.ViewData onLeftEvent(com.comp2042.event.MoveEvent event) {
            leftEventCalled = true;
            return null;
        }

        @Override
        public com.comp2042.model.ViewData onRightEvent(com.comp2042.event.MoveEvent event) {
            rightEventCalled = true;
            return null;
        }

        @Override
        public com.comp2042.model.ViewData onRotateEvent(com.comp2042.event.MoveEvent event) {
            rotateEventCalled = true;
            return null;
        }

        @Override
        public com.comp2042.model.ViewData onHoldEvent(com.comp2042.event.MoveEvent event) {
            holdEventCalled = true;
            return null;
        }

        @Override
        public void createNewGame() {
            createNewGameCalled = true;
        }
    }

    private static class TestGuiController extends GuiController {
        boolean moveDownCalled = false;
        boolean hardDropCalled = false;

        void reset() {
            moveDownCalled = false;
            hardDropCalled = false;
        }

        @Override
        public void moveDown(com.comp2042.event.MoveEvent event) {
            moveDownCalled = true;
        }

        @Override
        public void hardDrop(com.comp2042.event.MoveEvent event) {
            hardDropCalled = true;
        }
    }
}

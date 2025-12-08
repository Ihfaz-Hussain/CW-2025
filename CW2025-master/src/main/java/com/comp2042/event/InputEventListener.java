package com.comp2042.event;

import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

/**
 * Listener interface for handling game input events.
 * Implementations process movement, rotation, and game state changes.
 */
public interface InputEventListener {

    /**
     * Handles a downward movement event.
     *
     * @param event the move event containing type and source
     * @return result data containing cleared rows and updated view
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a hard drop event (instant drop to bottom).
     *
     * @param event the move event containing type and source
     * @return result data containing cleared rows and updated view
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Handles a left movement event.
     *
     * @param event the move event containing type and source
     * @return updated view data after the move
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a right movement event.
     *
     * @param event the move event containing type and source
     * @return updated view data after the move
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event.
     *
     * @param event the move event containing type and source
     * @return updated view data after rotation
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles a hold piece event.
     *
     * @param event the move event containing type and source
     * @return updated view data after the hold operation
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Creates a new game, resetting the board and score.
     */
    void createNewGame();
}

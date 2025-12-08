package com.comp2042.event;

import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

/**
 * Listener interface for handling game input events.
 * Implementations process movement, rotation, and game state changes.
 */
public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    void createNewGame();
}

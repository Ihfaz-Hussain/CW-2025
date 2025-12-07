package com.comp2042.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveEventTest {

    @Test
    void testConstructorAndGetEventType() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventType.LEFT, event.getEventType());
    }

    @Test
    void testConstructorAndGetEventSource() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.THREAD);
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testDownEventFromUser() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void testRotateEventFromThread() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.THREAD);
        assertEquals(EventType.ROTATE, event.getEventType());
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testHardDropEvent() {
        MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
        assertEquals(EventType.HARD_DROP, event.getEventType());
    }

    @Test
    void testHoldEvent() {
        MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
        assertEquals(EventType.HOLD, event.getEventType());
    }
}

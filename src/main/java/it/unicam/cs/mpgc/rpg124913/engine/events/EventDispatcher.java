package it.unicam.cs.mpgc.rpg124913.engine.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern (Subject): Manages the registration and notification of listeners.
 * It serves as the central communication bus for the game engine, broadcasting GameEvents
 * to all subscribed observers sequentially.
 */
public class EventDispatcher {
    private final List<EventListener> listeners;

    /**
     * Initializes an empty registry for event listeners.
     */
    public EventDispatcher() {
        this.listeners = new ArrayList<>();
    }

    /**
     * Subscribes a new listener to the event bus.
     *
     * @param listener The component wishing to receive event broadcasts.
     */
    public void subscribe(EventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Broadcasts an event to all registered listeners.
     *
     * @param event The event payload to distribute.
     */
    public void dispatch(GameEvent event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
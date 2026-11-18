package it.unicam.cs.mpgc.rpg124913.engine.events;

/**
 * Observer Pattern: Defines the contract for any component that needs to react to state
 * changes within the game engine, completely decoupling the UI or logging mechanisms
 * from the core domain logic.
 */
public interface EventListener {

    /**
     * Callback invoked when a game event is dispatched.
     *
     * @param event The immutable event data.
     */
    void onEvent(GameEvent event);
}
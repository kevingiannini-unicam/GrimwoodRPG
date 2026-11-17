package it.unicam.cs.mpgc.rpg124913.engine.actions;

import it.unicam.cs.mpgc.rpg124913.entities.core.GameEntity;

/**
 * Defines the contract for any executable command within the game loop.
 * Utilizing the Command Pattern ensures that behaviors are entirely decoupled
 * from the entities invoking them. This architectural choice guarantees maximum
 * extensibility, allowing new mechanics (e.g., spells, item usage) to be introduced
 * without modifying existing combat logic.
 */
public interface GameAction {

    /**
     * Executes the specific logic of this action.
     *
     * @param initiator The entity triggering the action.
     * @param target    The entity receiving the effects of the action.
     */
    void execute(GameEntity initiator, GameEntity target);

    /**
     * Provides a narrative description of the action being performed.
     * Useful for UI logs and telemetry.
     *
     * @return A formatted string describing the action.
     */
    String getCombatLogMessage();
}
package it.unicam.cs.mpgc.rpg124913.engine;

import it.unicam.cs.mpgc.rpg124913.engine.actions.GameAction;
import it.unicam.cs.mpgc.rpg124913.engine.events.EventDispatcher;
import it.unicam.cs.mpgc.rpg124913.engine.events.GameEvent;
import it.unicam.cs.mpgc.rpg124913.engine.exploration.GrimNode;
import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import it.unicam.cs.mpgc.rpg124913.entities.Fiend;

/**
 * The central orchestrator of the game's mechanics.
 * It strictly relies on the EventDispatcher to notify external systems (like the UI)
 * about state changes, achieving a highly cohesive and loosely coupled architecture.
 */
public class GameEngine {
    private final BeastHunter player;
    private final EventDispatcher dispatcher;
    private GrimNode currentNode;

    public GameEngine(BeastHunter player, EventDispatcher dispatcher) {
        this.player = player;
        this.dispatcher = dispatcher;
    }

    /**
     * Transitions the player into a new node and broadcasts the exploration event.
     *
     * @param targetNode The node to enter.
     */
    public void travelTo(GrimNode targetNode) {
        this.currentNode = targetNode;
        dispatcher.dispatch(new GameEvent("EXPLORATION",
                "Traveled to " + targetNode.getLocationName() + ". " + targetNode.getAtmosphere()));
    }

    public GrimNode getCurrentNode() {
        return currentNode;
    }

    /**
     * Resolves an action triggered by the player against the current encounter,
     * handling automated counter-attacks and broadcasting the outcomes.
     *
     * @param playerAction The specific command to execute.
     */
    public void processPlayerTurn(GameAction playerAction) {
        if (currentNode == null || player.isDead()) return;

        currentNode.getEncounter().ifPresentOrElse(fiend -> {
            if (fiend.isDead()) {
                dispatcher.dispatch(new GameEvent("COMBAT", "The beast is already dead."));
                return;
            }

            // Execute Player Action
            playerAction.execute(player, fiend);
            dispatcher.dispatch(new GameEvent("COMBAT", playerAction.getCombatLogMessage()));

            // Enemy Counter-Attack if still alive
            if (!fiend.isDead()) {
                dispatcher.dispatch(new GameEvent("COMBAT",
                        fiend.name() + " retaliates for " + fiend.damageOutput() + " damage!"));
                player.applyDamage(fiend.damageOutput());
            } else {
                dispatcher.dispatch(new GameEvent("COMBAT", fiend.name() + " has been slain."));
            }

            if (player.isDead()) {
                dispatcher.dispatch(new GameEvent("SYSTEM", "The Hunter has fallen. Game Over."));
            }
        }, () -> dispatcher.dispatch(new GameEvent("EXPLORATION", "There is nothing to attack here.")));
    }
}
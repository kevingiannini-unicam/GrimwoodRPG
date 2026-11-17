package it.unicam.cs.mpgc.rpg124913.engine.actions;

import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import it.unicam.cs.mpgc.rpg124913.entities.core.GameEntity;

/**
 * A specialized action representing the psychological toll of the cursed forest.
 * Specifically targets the BeastHunter's sanity. Demonstrates how the Command Pattern
 * handles highly specific logic without polluting generic combat managers.
 */
public class EldritchHorrorAction implements GameAction {
    private final int sanityDrainAmount;
    private String logMessage = "";

    /**
     * Constructs the horror action with a fixed sanity penalty.
     *
     * @param sanityDrainAmount The amount of sanity to be drained from the target.
     */
    public EldritchHorrorAction(int sanityDrainAmount) {
        this.sanityDrainAmount = sanityDrainAmount;
    }

    @Override
    public void execute(GameEntity initiator, GameEntity target) {
        if (target instanceof BeastHunter hunter) {
            hunter.drainSanity(sanityDrainAmount);
            this.logMessage = String.format("%s emits a maddening screech! %s loses %d Sanity.",
                    initiator.name(), hunter.name(), sanityDrainAmount);
        } else {
            this.logMessage = initiator.name() + " tries to induce madness, but it has no effect.";
        }
    }

    @Override
    public String getCombatLogMessage() {
        return this.logMessage;
    }
}
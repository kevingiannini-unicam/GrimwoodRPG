package it.unicam.cs.mpgc.rpg124913.engine.actions;

import it.unicam.cs.mpgc.rpg124913.entities.core.DamageDealer;
import it.unicam.cs.mpgc.rpg124913.entities.core.DamageReceiver;
import it.unicam.cs.mpgc.rpg124913.entities.core.GameEntity;

/**
 * Represents a standard physical strike.
 * It safely downcasts the entities to their specific capability interfaces
 * (DamageDealer and DamageReceiver) to calculate and apply damage.
 * If the entities do not support these capabilities, the action gracefully aborts.
 */
public class PhysicalStrike implements GameAction {
    private String logMessage = "";

    @Override
    public void execute(GameEntity initiator, GameEntity target) {
        if (initiator instanceof DamageDealer dealer && target instanceof DamageReceiver receiver) {
            int damage = dealer.damageOutput();
            receiver.applyDamage(damage);

            this.logMessage = String.format("%s strikes %s for %d physical damage.",
                    initiator.name(), target.name(), damage);
        } else {
            this.logMessage = "The physical strike failed: invalid target or initiator.";
        }
    }

    @Override
    public String getCombatLogMessage() {
        return this.logMessage;
    }
}
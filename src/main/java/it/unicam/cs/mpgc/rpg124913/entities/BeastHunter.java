package it.unicam.cs.mpgc.rpg124913.entities;

import it.unicam.cs.mpgc.rpg124913.entities.core.DamageDealer;
import it.unicam.cs.mpgc.rpg124913.entities.core.DamageReceiver;
import it.unicam.cs.mpgc.rpg124913.entities.core.GameEntity;
import it.unicam.cs.mpgc.rpg124913.entities.components.Vitality;

/**
 * Represents the player-controlled protagonist exploring the Grimwood.
 * This class acts as an aggregate root, composing various capabilities (DamageReceiver,
 * DamageDealer) and delegating health management to the encapsulated Vitality component.
 * It also introduces a unique 'Sanity' metric critical to the Dark Fantasy setting.
 */
public class BeastHunter implements GameEntity, DamageReceiver, DamageDealer {
    private final String name;
    private final Vitality vitality;
    private final int attackPower;
    private int sanity;

    /**
     * Constructs a new BeastHunter with specified physical and psychological limits.
     *
     * @param name        The moniker of the hunter.
     * @param maxHp       The structural physical limit.
     * @param attackPower The raw damage capability.
     * @param maxSanity   The mental resilience threshold.
     */
    public BeastHunter(String name, int maxHp, int attackPower, int maxSanity) {
        this.name = name;
        this.vitality = new Vitality(maxHp);
        this.attackPower = attackPower;
        this.sanity = maxSanity;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return "A lone hunter traversing the cursed Grimwood, armed with silver and grit.";
    }

    @Override
    public void applyDamage(int amount) {
        vitality.reduce(amount);
    }

    @Override
    public boolean isDead() {
        return vitality.isDepleted();
    }

    @Override
    public int currentHealth() {
        return vitality.getCurrent();
    }

    @Override
    public int damageOutput() {
        return attackPower;
    }

    /**
     * Applies psychological trauma, reducing the hunter's sanity metric.
     *
     * @param amount The sanity points to be drained. Ignored if negative.
     */
    public void drainSanity(int amount) {
        if (amount > 0) {
            this.sanity = Math.max(0, this.sanity - amount);
        }
    }

    /**
     * Exposes the current sanity level.
     *
     * @return The active sanity points.
     */
    public int getSanity() {
        return sanity;
    }
}
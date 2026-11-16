package it.unicam.cs.mpgc.rpg124913.entities;

import it.unicam.cs.mpgc.rpg124913.entities.core.DamageDealer;
import it.unicam.cs.mpgc.rpg124913.entities.core.DamageReceiver;
import it.unicam.cs.mpgc.rpg124913.entities.core.GameEntity;
import it.unicam.cs.mpgc.rpg124913.entities.components.Vitality;

/**
 * Represents the antagonistic entities dwelling within the forest.
 * Similar to the BeastHunter, it aggregates capabilities and delegates health tracking
 * to the Vitality component, demonstrating high code reuse without rigid inheritance trees.
 */
public class Fiend implements GameEntity, DamageReceiver, DamageDealer {
    private final String name;
    private final String classification;
    private final Vitality vitality;
    private final int damage;

    /**
     * Constructs a hostile entity with specific traits.
     *
     * @param name           The identifier of the fiend.
     * @param classification The biological or supernatural category (e.g., "Beast", "Undead").
     * @param maxHp          The structural physical limit.
     * @param damage         The standard physical damage output.
     */
    public Fiend(String name, String classification, int maxHp, int damage) {
        this.name = name;
        this.classification = classification;
        this.vitality = new Vitality(maxHp);
        this.damage = damage;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return "A terrifying " + classification + " born from the encroaching mist.";
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
        return damage;
    }
}
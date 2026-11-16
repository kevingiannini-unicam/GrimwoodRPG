package it.unicam.cs.mpgc.rpg124913.entities.core;

/**
 * Defines the capability of an entity to sustain and process damage.
 * Segregating this behavior from the base GameEntity allows the architecture to support
 * destructible but non-combative elements (e.g., a wooden barricade or a fragile relic)
 * without forcing them to implement attack mechanics.
 */
public interface DamageReceiver {

    /**
     * Processes incoming damage, reducing the entity's internal health state.
     *
     * @param amount The raw numerical value of the damage to be applied. Must be non-negative.
     */
    void applyDamage(int amount);

    /**
     * Evaluates the current life state of the entity.
     *
     * @return True if the entity's health is depleted; false otherwise.
     */
    boolean isDead();

    /**
     * Exposes the current numerical health value, primarily for UI bindings and telemetry.
     *
     * @return The current health points.
     */
    int currentHealth();
}
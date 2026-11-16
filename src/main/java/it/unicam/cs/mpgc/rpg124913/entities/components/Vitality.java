package it.unicam.cs.mpgc.rpg124913.entities.components;

/**
 * Encapsulates the lifecycle and health metrics of an entity.
 * Utilizing Composition over Inheritance, this component can be injected into any
 * entity requiring health mechanics, preventing deep and rigid class hierarchies.
 * It acts as the single source of truth for health validation and boundary checks.
 */
public class Vitality {
    private final int maxHealth;
    private int currentHealth;

    /**
     * Initializes the vitality state, setting the current health to the maximum capacity.
     *
     * @param maxHealth The maximum allowable health points.
     */
    public Vitality(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * Subtracts a specified amount from the current health, ensuring it does not drop below zero.
     *
     * @param amount The damage to subtract. Ignored if negative.
     */
    public void reduce(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }
    }

    /**
     * Adds a specified amount to the current health, capping it at the maximum capacity.
     *
     * @param amount The healing to apply. Ignored if negative.
     */
    public void restore(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.min(maxHealth, this.currentHealth + amount);
        }
    }

    /**
     * Checks if the vitality has been completely exhausted.
     *
     * @return True if current health is zero; otherwise false.
     */
    public boolean isDepleted() {
        return currentHealth <= 0;
    }

    /**
     * Retrieves the current health value.
     *
     * @return The active health points.
     */
    public int getCurrent() {
        return currentHealth;
    }

    /**
     * Retrieves the maximum structural health limit.
     *
     * @return The maximum health points.
     */
    public int getMax() {
        return maxHealth;
    }
}
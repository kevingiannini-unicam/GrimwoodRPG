package it.unicam.cs.mpgc.rpg124913.entities.core;

/**
 * Serves as the foundational contract for any identifiable object within the game world.
 * By abstracting the core identity (name and description), the engine can handle
 * diverse objects—ranging from NPCs to environmental obstacles—through a unified reference,
 * ensuring high extensibility for future additions.
 */
public interface GameEntity {

    /**
     * Retrieves the display name of the entity.
     *
     * @return A string representing the entity's name.
     */
    String name();

    /**
     * Retrieves narrative or atmospheric details about the entity.
     *
     * @return A string containing the entity's lore or physical description.
     */
    String description();
}
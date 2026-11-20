package it.unicam.cs.mpgc.rpg124913.storage;

/**
 * Immutable data carrier (Memento/Snapshot) representing the serializable state
 * of a game session. Using a Java Record ensures thread-safety, immutability,
 * and native compatibility with Jackson's deserialization engine without
 * polluting core domain entities with framework-specific annotations.
 *
 * @param name                The hunter's name.
 * @param maxHp               The hunter's maximum health points.
 * @param currentHp           The hunter's active health points.
 * @param attackPower         The hunter's offensive power.
 * @param sanity              The hunter's remaining sanity.
 * @param currentLocationName The name of the node currently occupied by the player.
 */
public record HunterSnapshot(
        String name,
        int maxHp,
        int currentHp,
        int attackPower,
        int sanity,
        String currentLocationName
) {
}
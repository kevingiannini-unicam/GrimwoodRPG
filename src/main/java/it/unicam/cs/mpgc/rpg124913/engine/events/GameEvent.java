package it.unicam.cs.mpgc.rpg124913.engine.events;

/**
 * Represent an immutable data carrier for game events.
 * Utilizing records provides a concise syntax and inherent thread-safety for event payloads
 * broadcasted across the architecture.
 *
 * @param type    The categorical type of the event (e.g., "COMBAT", "EXPLORATION").
 * @param message The detailed payload or narrative description.
 */
public record GameEvent(String type, String message) {
}
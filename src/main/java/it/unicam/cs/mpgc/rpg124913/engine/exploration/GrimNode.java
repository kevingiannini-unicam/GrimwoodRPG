package it.unicam.cs.mpgc.rpg124913.engine.exploration;

import it.unicam.cs.mpgc.rpg124913.entities.Fiend;
import java.util.Optional;

/**
 * Represents a discrete location or waypoint within the cursed Grimwood.
 * Encapsulates environmental data and potential hostile encounters.
 */
public class GrimNode {
    private final String locationName;
    private final String atmosphere;
    private Fiend lurkingFiend;

    /**
     * Constructs a new explorable node.
     *
     * @param locationName The geographical or mystical name of the location.
     * @param atmosphere   The narrative description of the surroundings.
     */
    public GrimNode(String locationName, String atmosphere) {
        this.locationName = locationName;
        this.atmosphere = atmosphere;
    }

    public String getLocationName() { return locationName; }

    public String getAtmosphere() { return atmosphere; }

    /**
     * Injects a hostile entity into this node.
     *
     * @param fiend The monster to inhabit the node.
     */
    public void setEncounter(Fiend fiend) {
        this.lurkingFiend = fiend;
    }

    /**
     * Safely retrieves the lurking fiend, utilizing Optional to handle safe null-checks
     * natively without throwing NullPointerExceptions.
     *
     * @return An Optional containing the fiend if present, or an empty Optional.
     */
    public Optional<Fiend> getEncounter() {
        return Optional.ofNullable(lurkingFiend);
    }
}
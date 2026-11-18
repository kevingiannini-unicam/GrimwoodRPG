package it.unicam.cs.mpgc.rpg124913.entities;

import it.unicam.cs.mpgc.rpg124913.entities.components.Vitality;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the state integrity and boundary constraints of the Vitality component
 * and the aggregate entities (BeastHunter, Fiend) relying on composition.
 */
@DisplayName("Entity & Composition Mechanics Tests")
class EntitiesTest {

    @Nested
    @DisplayName("Vitality Component Constraints")
    class VitalityTests {

        @Test
        @DisplayName("Should prevent health from dropping below zero or exceeding max capacity")
        void shouldEnforceHealthBoundaries() {
            Vitality vitality = new Vitality(50);

            vitality.reduce(120);
            assertEquals(0, vitality.getCurrent(), "Health must clamp at zero.");
            assertTrue(vitality.isDepleted(), "Vitality must report depleted state at zero.");

            vitality.restore(999);
            assertEquals(50, vitality.getCurrent(), "Restored health must not exceed maxHealth.");
            assertFalse(vitality.isDepleted(), "Vitality should not be depleted after restoration.");
        }

        @Test
        @DisplayName("Should ignore negative inputs for damage and healing")
        void shouldIgnoreNegativeValues() {
            Vitality vitality = new Vitality(100);
            vitality.reduce(-25);
            assertEquals(100, vitality.getCurrent(), "Negative damage must be ignored.");

            vitality.reduce(40);
            vitality.restore(-10);
            assertEquals(60, vitality.getCurrent(), "Negative healing must be ignored.");
        }
    }

    @Nested
    @DisplayName("BeastHunter Specific Mechanics")
    class HunterTests {

        @Test
        @DisplayName("Should accurately drain sanity without dropping below zero")
        void shouldDrainSanitySafely() {
            BeastHunter hunter = new BeastHunter("Vaelen", 100, 15, 40);

            hunter.drainSanity(15);
            assertEquals(25, hunter.getSanity(), "Sanity should decrease by exact drain amount.");

            hunter.drainSanity(100);
            assertEquals(0, hunter.getSanity(), "Sanity must clamp at zero.");
        }
    }
}
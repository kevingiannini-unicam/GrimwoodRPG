package it.unicam.cs.mpgc.rpg124913.storage;

import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the JSON serialization round-trip and entity reconstruction
 * managed by JacksonStorageGateway.
 */
@DisplayName("Jackson Persistence Gateway Tests")
class JacksonStorageGatewayTest {

    @Test
    @DisplayName("Should accurately persist and retrieve a HunterSnapshot via JSON")
    void shouldPersistAndRetrieveSnapshot(@TempDir Path tempDir) throws IOException {
        StorageGateway gateway = new JacksonStorageGateway();
        Path saveFile = tempDir.resolve("grimwood_save.json");

        HunterSnapshot original = new HunterSnapshot("Vaelen", 100, 72, 25, 35, "Ashen Chapel");

        gateway.persist(original, saveFile);
        assertTrue(Files.exists(saveFile), "Save file should exist on disk.");

        HunterSnapshot loaded = gateway.retrieve(saveFile);
        assertEquals(original, loaded, "Deserialized snapshot must match the original record.");

        // Verify faithful domain entity reconstruction
        BeastHunter restoredHunter = BeastHunter.restoreState(
                loaded.name(), loaded.maxHp(), loaded.currentHp(), loaded.attackPower(), loaded.sanity()
        );
        assertEquals(72, restoredHunter.currentHealth());
        assertEquals(35, restoredHunter.getSanity());
    }
}
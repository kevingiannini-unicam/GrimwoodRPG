package it.unicam.cs.mpgc.rpg124913.storage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines the persistence contract for saving and loading game snapshots.
 * High-level modules depend exclusively on this abstraction, allowing the underlying
 * storage medium (JSON, XML, SQLite, or Cloud API) to be replaced seamlessly.
 */
public interface StorageGateway {

    /**
     * Writes the provided snapshot to the specified target path.
     *
     * @param snapshot   The immutable state to persist.
     * @param targetPath The destination file system path.
     * @throws IOException If an I/O error occurs during serialization or file writing.
     */
    void persist(HunterSnapshot snapshot, Path targetPath) throws IOException;

    /**
     * Reads and reconstructs a snapshot from the specified source path.
     *
     * @param sourcePath The path of the persisted save file.
     * @return The deserialized HunterSnapshot.
     * @throws IOException If the file cannot be read or contains malformed data.
     */
    HunterSnapshot retrieve(Path sourcePath) throws IOException;
}
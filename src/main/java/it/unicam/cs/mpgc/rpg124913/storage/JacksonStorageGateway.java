package it.unicam.cs.mpgc.rpg124913.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Concrete implementation of StorageGateway backed by the Jackson Databind library.
 * Handles JSON serialization and deserialization with human-readable indentation.
 */
public class JacksonStorageGateway implements StorageGateway {
    private final ObjectMapper mapper;

    /**
     * Initializes the gateway with a pre-configured Jackson ObjectMapper.
     */
    public JacksonStorageGateway() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void persist(HunterSnapshot snapshot, Path targetPath) throws IOException {
        mapper.writeValue(targetPath.toFile(), snapshot);
    }

    @Override
    public HunterSnapshot retrieve(Path sourcePath) throws IOException {
        return mapper.readValue(sourcePath.toFile(), HunterSnapshot.class);
    }
}
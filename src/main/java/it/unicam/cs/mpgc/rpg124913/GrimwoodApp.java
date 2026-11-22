package it.unicam.cs.mpgc.rpg124913;

import it.unicam.cs.mpgc.rpg124913.engine.events.EventDispatcher;
import it.unicam.cs.mpgc.rpg124913.engine.exploration.GrimNode;
import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import it.unicam.cs.mpgc.rpg124913.entities.Fiend;
import it.unicam.cs.mpgc.rpg124913.storage.JacksonStorageGateway;
import it.unicam.cs.mpgc.rpg124913.storage.StorageGateway;
import it.unicam.cs.mpgc.rpg124913.ui.GrimwoodFrame;

import javax.swing.*;
import java.util.List;

/**
 * Application entry point and Composition Root for Grimwood: The Cursed Forest.
 * Responsible solely for instantiating domain entities, wiring architectural dependencies,
 * and launching the graphical interface on the Swing Event Dispatch Thread.
 */
public class GrimwoodApp {

    public static void main(String[] args) {
        // Initialize core domain entities and world topology
        BeastHunter hunter = new BeastHunter("Vaelen", 100, 22, 50);

        GrimNode outskirts = new GrimNode("Misty Outskirts", "Pale fog clings to rotting pine roots.");
        outskirts.setEncounter(new Fiend("Hollow Wolf", "Beast", 40, 10));

        GrimNode chapel = new GrimNode("Ashen Chapel", "Collapsed stone arches echo with distant whispers.");
        chapel.setEncounter(new Fiend("Dread Wraith", "Specter", 65, 16));

        GrimNode altar = new GrimNode("The Bleeding Altar", "An ancient monolith pulses with dark energy.");
        altar.setEncounter(new Fiend("Grimwood Stalker", "Eldritch Horror", 90, 22));

        List<GrimNode> forestPath = List.of(outskirts, chapel, altar);

        // Wire infrastructure components
        EventDispatcher dispatcher = new EventDispatcher();
        StorageGateway storageGateway = new JacksonStorageGateway();

        // Launch GUI safely on the Swing EDT
        SwingUtilities.invokeLater(() -> {
            GrimwoodFrame frame = new GrimwoodFrame(hunter, dispatcher, storageGateway, forestPath);
            frame.setVisible(true);
        });
    }
}
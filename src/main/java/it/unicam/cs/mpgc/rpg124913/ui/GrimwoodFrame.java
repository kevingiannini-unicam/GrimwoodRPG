package it.unicam.cs.mpgc.rpg124913.ui;

import it.unicam.cs.mpgc.rpg124913.engine.GameEngine;
import it.unicam.cs.mpgc.rpg124913.engine.actions.PhysicalStrike;
import it.unicam.cs.mpgc.rpg124913.engine.events.EventDispatcher;
import it.unicam.cs.mpgc.rpg124913.engine.events.EventListener;
import it.unicam.cs.mpgc.rpg124913.engine.events.GameEvent;
import it.unicam.cs.mpgc.rpg124913.engine.exploration.GrimNode;
import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import it.unicam.cs.mpgc.rpg124913.storage.HunterSnapshot;
import it.unicam.cs.mpgc.rpg124913.storage.StorageGateway;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Primary graphical user interface built with Java Swing.
 * Implements EventListener to operate purely as a reactive observer of the GameEngine.
 * Contains no business logic; it strictly translates user interactions into engine commands
 * and renders broadcasted GameEvents onto the Swing Event Dispatch Thread (EDT).
 */
public class GrimwoodFrame extends JFrame implements EventListener {
    private static final Path SAVE_PATH = Path.of("grimwood_save.json");

    private final EventDispatcher dispatcher;
    private final StorageGateway storageGateway;
    private final List<GrimNode> forestPath;

    private BeastHunter player;
    private GameEngine engine;
    private int currentNodeIndex = 0;

    private final JLabel statusLabel;
    private final JTextArea logArea;
    private final JButton strikeButton;
    private final JButton travelButton;

    /**
     * Constructs and initializes the main game window, binding it to the core engine
     * and persistence abstractions.
     *
     * @param player         The initial player entity.
     * @param dispatcher     The central event bus to subscribe to.
     * @param storageGateway The persistence abstraction for saving/loading snapshots.
     * @param forestPath     The ordered sequence of explorable nodes in the forest.
     */
    public GrimwoodFrame(BeastHunter player, EventDispatcher dispatcher,
                         StorageGateway storageGateway, List<GrimNode> forestPath) {
        this.player = player;
        this.dispatcher = dispatcher;
        this.storageGateway = storageGateway;
        this.forestPath = forestPath;
        this.engine = new GameEngine(player, dispatcher);

        // Register this UI frame as an observer of engine events
        this.dispatcher.subscribe(this);

        setTitle("GRIMWOOD: The Cursed Forest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(24, 24, 28));

        // Top Panel: Player Telemetry
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        statusLabel.setForeground(new Color(220, 210, 195));
        statusLabel.setBorder(new EmptyBorder(12, 15, 12, 15));
        add(statusLabel, BorderLayout.NORTH);

        // Center Panel: Narrative Event Log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setBackground(new Color(16, 16, 20));
        logArea.setForeground(new Color(195, 205, 195));
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Action Controls
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBackground(new Color(24, 24, 28));
        controlPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        strikeButton = createStyledButton("Physical Strike");
        travelButton = createStyledButton("Advance Deeper");
        JButton saveButton = createStyledButton("Save Progress");
        JButton loadButton = createStyledButton("Load Progress");

        strikeButton.addActionListener(e -> {
            engine.processPlayerTurn(new PhysicalStrike());
            refreshTelemetry();
        });

        travelButton.addActionListener(e -> advanceToNextNode());
        saveButton.addActionListener(e -> handleSave());
        loadButton.addActionListener(e -> handleLoad());

        controlPanel.add(strikeButton);
        controlPanel.add(travelButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Enter the starting node
        engine.travelTo(forestPath.get(currentNodeIndex));
        refreshTelemetry();
    }

    /**
     * Reactive callback invoked by the EventDispatcher whenever a game event occurs.
     * Marshals UI updates safely onto the Swing Event Dispatch Thread.
     *
     * @param event The immutable event broadcasted by the engine.
     */
    @Override
    public void onEvent(GameEvent event) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(String.format("[%s] %s%n", event.type(), event.message()));
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    /**
     * Moves the player to the next node in the forest path, if available.
     */
    private void advanceToNextNode() {
        if (currentNodeIndex + 1 < forestPath.size()) {
            currentNodeIndex++;
            engine.travelTo(forestPath.get(currentNodeIndex));
            refreshTelemetry();
        } else {
            dispatcher.dispatch(new GameEvent("EXPLORATION", "You have reached the heart of Grimwood. No path leads further."));
        }
    }

    /**
     * Captures the current player state into an immutable snapshot and persists it.
     */
    private void handleSave() {
        String location = engine.getCurrentNode() != null ? engine.getCurrentNode().getLocationName() : "Unknown";
        HunterSnapshot snapshot = new HunterSnapshot(
                player.name(),
                player.maxHealth(),
                player.currentHealth(),
                player.damageOutput(),
                player.getSanity(),
                location
        );
        try {
            storageGateway.persist(snapshot, SAVE_PATH);
            dispatcher.dispatch(new GameEvent("SYSTEM", "Progress saved to " + SAVE_PATH.getFileName()));
        } catch (IOException ex) {
            dispatcher.dispatch(new GameEvent("ERROR", "Failed to save progress: " + ex.getMessage()));
        }
    }

    /**
     * Retrieves a persisted snapshot, reconstructs the domain entity, and rebinds the engine.
     */
    private void handleLoad() {
        try {
            HunterSnapshot loaded = storageGateway.retrieve(SAVE_PATH);
            this.player = BeastHunter.restoreState(
                    loaded.name(), loaded.maxHp(), loaded.currentHp(), loaded.attackPower(), loaded.sanity()
            );
            this.engine = new GameEngine(this.player, this.dispatcher);

            // Restore the matching node index if present in the current forest path
            for (int i = 0; i < forestPath.size(); i++) {
                if (forestPath.get(i).getLocationName().equals(loaded.currentLocationName())) {
                    currentNodeIndex = i;
                    break;
                }
            }
            dispatcher.dispatch(new GameEvent("SYSTEM", "Progress restored for Hunter " + player.name() + "."));
            engine.travelTo(forestPath.get(currentNodeIndex));
            refreshTelemetry();
        } catch (IOException ex) {
            dispatcher.dispatch(new GameEvent("ERROR", "Unable to load save file: " + ex.getMessage()));
        }
    }

    /**
     * Updates the top status bar and disables combat/travel controls if the hunter is dead.
     */
    private void refreshTelemetry() {
        String location = engine.getCurrentNode() != null ? engine.getCurrentNode().getLocationName() : "None";
        statusLabel.setText(String.format("HUNTER: %s  |  HP: %d/%d  |  SANITY: %d  |  LOCATION: %s",
                player.name(), player.currentHealth(), player.maxHealth(), player.getSanity(), location));

        boolean alive = !player.isDead();
        strikeButton.setEnabled(alive);
        travelButton.setEnabled(alive);
    }

    /**
     * Utility factory for creating consistently styled dark-theme buttons.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(new Color(48, 48, 56));
        button.setForeground(new Color(230, 230, 230));
        button.setFocusPainted(false);
        return button;
    }
}
package it.unicam.cs.mpgc.rpg124913.engine;

import it.unicam.cs.mpgc.rpg124913.engine.actions.EldritchHorrorAction;
import it.unicam.cs.mpgc.rpg124913.engine.actions.PhysicalStrike;
import it.unicam.cs.mpgc.rpg124913.engine.events.EventDispatcher;
import it.unicam.cs.mpgc.rpg124913.engine.events.GameEvent;
import it.unicam.cs.mpgc.rpg124913.engine.exploration.GrimNode;
import it.unicam.cs.mpgc.rpg124913.entities.BeastHunter;
import it.unicam.cs.mpgc.rpg124913.entities.Fiend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validates the interaction between the EventDispatcher, Command Pattern actions,
 * and the GameEngine turn resolution loop.
 */
@DisplayName("GameEngine & Event-Driven Architecture Tests")
class GameEngineTest {
    private BeastHunter hunter;
    private Fiend fiend;
    private GrimNode node;
    private EventDispatcher dispatcher;
    private GameEngine engine;
    private List<GameEvent> capturedEvents;

    @BeforeEach
    void initEnvironment() {
        hunter = new BeastHunter("Vaelen", 100, 25, 50);
        fiend = new Fiend("Hollow Wolf", "Beast", 40, 12);
        node = new GrimNode("Whispering Glade", "Twisted oaks block the moonlight.");

        dispatcher = new EventDispatcher();
        capturedEvents = new ArrayList<>();
        // Subscribe an in-memory lambda listener to inspect broadcasted events
        dispatcher.subscribe(capturedEvents::add);

        engine = new GameEngine(hunter, dispatcher);
    }

    @Test
    @DisplayName("Should dispatch an EXPLORATION event when traveling to a new node")
    void shouldDispatchEventOnTravel() {
        engine.travelTo(node);

        assertEquals(node, engine.getCurrentNode());
        assertEquals(1, capturedEvents.size());
        assertEquals("EXPLORATION", capturedEvents.get(0).type());
        assertTrue(capturedEvents.get(0).message().contains("Whispering Glade"));
    }

    @Test
    @DisplayName("Should execute PhysicalStrike, trigger fiend counter-attack, and broadcast COMBAT events")
    void shouldResolveCombatTurnAndRetaliation() {
        node.setEncounter(fiend);
        engine.travelTo(node);
        capturedEvents.clear(); // Isolate combat events

        engine.processPlayerTurn(new PhysicalStrike());

        assertEquals(15, fiend.currentHealth(), "Fiend should take 25 damage (40 -> 15).");
        assertEquals(88, hunter.currentHealth(), "Hunter should take 12 retaliation damage (100 -> 88).");
        assertEquals(2, capturedEvents.size(), "Two combat events (strike and retaliation) should be emitted.");
        assertTrue(capturedEvents.stream().allMatch(e -> e.type().equals("COMBAT")));
    }

    @Test
    @DisplayName("Should prevent counter-attack and emit slain event when fiend dies")
    void shouldNotRetaliateWhenSlain() {
        Fiend weakFiend = new Fiend("Wisp", "Spirit", 20, 50);
        node.setEncounter(weakFiend);
        engine.travelTo(node);
        capturedEvents.clear();

        engine.processPlayerTurn(new PhysicalStrike());

        assertTrue(weakFiend.isDead(), "Weak fiend should be dead after 25 damage.");
        assertEquals(100, hunter.currentHealth(), "Dead fiend must not retaliate.");
        assertTrue(capturedEvents.stream().anyMatch(e -> e.message().contains("has been slain")));
    }

    @Test
    @DisplayName("Should drain hunter sanity when EldritchHorrorAction is executed")
    void shouldExecuteEldritchHorrorAction() {
        EldritchHorrorAction horror = new EldritchHorrorAction(20);
        horror.execute(fiend, hunter);

        assertEquals(30, hunter.getSanity(), "Hunter sanity should decrease by 20.");
        assertTrue(horror.getCombatLogMessage().contains("loses 20 Sanity"));
    }
}
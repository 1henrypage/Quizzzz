package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDataTest {

    PlayerData p1;
    PlayerData p2;
    PlayerData p3;
    @BeforeEach
    void init() {
        p1 = new PlayerData("Alice");
        p2 = new PlayerData("Alice");
        p3 = new PlayerData("Bob");
    }


    @Test
    void getPlayerName() {
        assertEquals("Alice",p1.getPlayerName());
        assertEquals("Bob",p3.getPlayerName());
    }

    @Test
    void addScore() {
        assertEquals(0L,p1.getScore());
        p1.addScore(32);
        assertEquals(32L,p1.getScore());
        assertEquals(0L,p2.getScore());
        p1.addScore(32L);
        assertEquals(64L,p1.getScore());
        p2.addScore(16L);
        assertEquals(16L,p2.getScore());
    }

    @Test
    void getScore() {
        assertEquals(0L,p1.getScore());
        p1.addScore(999L);
        assertEquals(999L,p1.getScore());
        assertEquals(0L,p2.getScore());
    }

    @Test
    void jokerHasBeenUsed() {
        //TODO
    }

    @Test
    void useJoker() {
        //TODO
    }

    @Test
    void hasSameName() {
        assertEquals(p1,p2);
        assertTrue(p1.hasSameName(p2));
        p1.addScore(40L);
        assertNotEquals(p1,p2);
        assertTrue(p1.hasSameName(p2));
    }

    @Test
    void testEquals() {
        assertEquals(p1,p2);
        assertNotEquals(p1,p3);
    }

    @Test
    void testHashCode() {
        assertEquals(p1,p2);
        assertEquals(p1.hashCode(),p2.hashCode());
        assertNotEquals(p1.hashCode(),p3.hashCode());
    }

    @Test
    void testToString() {
        var actual = p1.toString();
        assertTrue(actual.contains(PlayerData.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("score"));
    }
}
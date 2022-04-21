package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JokerUseTest {

    JokerUse j1, j2, j3;

    @BeforeEach
    void setUp() {
        j1 = new JokerUse(1L, "p1", JokerType.REDUCE_TIME);
        j2 = new JokerUse(2L, "p2", JokerType.DOUBLE_POINTS);
        j3 = new JokerUse(2L, "p2", JokerType.DOUBLE_POINTS);
    }

    @Test
    void getGameId() {
        assertEquals(1L, j1.getGameId());
    }

    @Test
    void getPlayerName() {
        assertEquals("p1", j1.getPlayerName());
    }

    @Test
    void getJokerType() {
        assertEquals(JokerType.REDUCE_TIME, j1.getJokerType());
    }

    @Test
    void testEquals() {
        assertEquals(j2, j3);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(j1, j3);
    }

    @Test
    void testHashCode() {
        assertEquals(j2.hashCode(), j3.hashCode());
    }

    @Test
    void testHashCodeNotEquals() {
        assertNotEquals(j1.hashCode(), j3.hashCode());
    }

    @Test
    void testToString() {
        assertTrue(j1.toString().contains("p1"));
        assertTrue(j1.toString().contains("REDUCE_TIME"));
    }
}
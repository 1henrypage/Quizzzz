package client.scenes;

import commons.JokerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JokerDataTest {
    JokerData j1;

    @BeforeEach
    void init() {
        j1 = new JokerData("JokerText", JokerType.DOUBLE_POINTS, true, true, true, true, true);
    }

    @Test
    void isMc() {
        assertTrue(j1.isMc());
    }

    @Test
    void isEstimate() {
        assertTrue(j1.isEstimate());
    }

    @Test
    void isSp() {
        assertTrue(j1.isSp());
    }

    @Test
    void isMp() {
        assertTrue(j1.isMp());
    }

    @Test
    void setUsed() {
        j1.setUsed(false);
        assertFalse(j1.isUsed());
    }

    @Test
    void isUsed() {
        assertTrue(j1.isUsed());
    }

    @Test
    void getText() {
        assertEquals("JokerText", j1.getText());
    }

    @Test
    void getType() {
        assertEquals(JokerType.DOUBLE_POINTS, j1.getType());
    }

    @Test
    void testEquals1() {
        JokerData j2 = new JokerData("JokerText", JokerType.DOUBLE_POINTS, true, true, true, true, true);
        assertEquals(j1, j2);
    }

    @Test
    void testHashCode() {
        JokerData j2 = new JokerData("JokerText", JokerType.DOUBLE_POINTS, true, true, true, true, true);
        JokerData j3 = new JokerData("JokerText", JokerType.DOUBLE_POINTS, true, false, true, true, true);
        assertEquals(j1.hashCode(),j2.hashCode());
        assertNotEquals(j1.hashCode(),j3.hashCode());
    }

    @Test
    void hasToString() {
        var actual = j1.toString();
        assertTrue(actual.contains("JokerText"));
    }
}
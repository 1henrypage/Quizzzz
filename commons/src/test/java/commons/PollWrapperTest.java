package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PollWrapperTest {

    PollWrapper p;
    PollWrapper p2;
    PollWrapper p3;

    @BeforeEach
    void setUp() {
        List<String> l = new ArrayList<>();
        l.add(":D");
        l.add(":X");
        l.add(":S");
        p = new PollWrapper(l, new PlayerData("Otto"));
        p3 = new PollWrapper(l, new PlayerData("Otto"));
        p2 = new PollWrapper(null, null);
    }

    @Test
    void getUiMessages() {
        assertNotNull(p.getUiMessages());
    }

    @Test
    void setUiMessages() {
        p2.setUiMessages(new ArrayList<>());
        assertNotNull(p2.getUiMessages());
    }

    @Test
    void getWhoInitiated() {
        assertNotNull(p.getWhoInitiated());
    }

    @Test
    void setWhoInitiated() {
        p2.setWhoInitiated(new PlayerData("null"));
        assertNotNull(p2.getWhoInitiated());
    }

    @Test
    void testEquals() {
        assertNotEquals(p2, p);
    }

    @Test
    void testHashCode() {
        assertEquals(p,p3);
        assertEquals(p.hashCode(),p3.hashCode());
        assertNotEquals(p.hashCode(),p2.hashCode());
    }

    @Test
    void testToString() {
        var actual = p.toString();
        assertTrue(actual.contains(PollWrapper.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("whoInitiated"));
    }
}
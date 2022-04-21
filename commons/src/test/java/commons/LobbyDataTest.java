package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyDataTest {

    LobbyData d1;
    LobbyData d2;
    LobbyData d3;
    List<PlayerData> testList;
    @BeforeEach
    void init() {
        List<PlayerData> playerDataList = List.of(
                new PlayerData("Alice"),
                        new PlayerData("Bob"));
        testList = playerDataList;

        d1 = new LobbyData(playerDataList,
                false,0L);
        d2 = new LobbyData(playerDataList,
                false,0L);
        d3 = new LobbyData(List.of
                (new PlayerData("Jay"),
                        new PlayerData("Lom")),
                true,0L);
    }

    @Test
    void constructorTest() {
        assertNotNull(d1);
    }

    @Test
    void getPlayerDataList() {
        assertEquals(testList,d1.getPlayerDataList());
        assertNotEquals(testList,d3.getPlayerDataList());
    }

    @Test
    void isInStartState() {
        assertFalse(d1.isInStartState());
        assertTrue(d3.isInStartState());

    }

    @Test
    void getAssignedGameID() {
        assertEquals(-1L,d1.getAssignedGameID());
        assertEquals(0L,d3.getAssignedGameID());
    }

    @Test
    void testEquals() {
        assertEquals(d1,d2);
        assertNotEquals(d1,d3);
    }

    @Test
    void testHashCode() {
        assertEquals(d1.hashCode(),d2.hashCode());
        assertNotEquals(d1.hashCode(),d3.hashCode());
    }

    @Test
    void hasToString() {
        var actual = d1.toString();
        assertTrue(actual.contains(LobbyData.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("playerDataList"));
    }
}
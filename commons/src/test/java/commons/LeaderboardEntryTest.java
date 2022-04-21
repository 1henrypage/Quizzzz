package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeaderboardEntryTest {

    /**
     * Adding checkstyle
     */
    @Test
    public void checkConstructor() {
        var le = new LeaderboardEntry("name", 100);
        assertEquals("name", le.getName());
        assertEquals(100, le.getScore());
    }

    /**
     * Adding checkstyle
     */
    @Test
    public void getNameTest(){
        var le = new LeaderboardEntry("name", 100);
        assertEquals("name", le.getName());
    }

    /**
     * Adding checkstyle
     */
    @Test
    public void getScoreTest(){
        var le = new LeaderboardEntry("name", 100);
        assertEquals(100, le.getScore());
    }

    /**
     * Adding checkstyle
     */
    @Test
    public void equalsHashCode() {
        var le1 = new LeaderboardEntry("name", 100);
        var le2 = new LeaderboardEntry("name", 100);
        assertEquals(le1, le2);
        assertEquals(le1.hashCode(), le2.hashCode());
    }

    /**
     * Adding checkstyle
     */
    @Test
    public void notEqualsHashCode() {
        var le1 = new LeaderboardEntry( "name1", 101);
        var le2 = new LeaderboardEntry("name2", 102);
        assertNotEquals(le1, le2);
        assertNotEquals(le1.hashCode(), le2.hashCode());
    }

    /**
     * Adding checkstyle
     */
    @Test
    public void hasToString() {
        var le1 = new LeaderboardEntry("name", 100).toString();
        assertTrue(le1.contains(LeaderboardEntry.class.getSimpleName()));
        assertTrue(le1.contains("\n"));
        assertTrue(le1.contains("name"));
    }

}

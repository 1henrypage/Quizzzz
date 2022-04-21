package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {
    Activity a1;
    Activity a2;
    Activity a3;

    @BeforeEach
    void setUp() {
        a1 = new Activity("02-shower", "/shower.png","Shower", 10.2,"example.com");
        a2 = new Activity("02-shower", "/shower.png","Shower", 10.2,"example.com");
        a3 = new Activity("05-flamethrower", "/flamethrower.png","Flamethrower", 99.3,"example.com");
    }

    @Test
    void constructorTest() {
        assertNotNull(a1);
        assertEquals("Shower",a1.getTitle());
    }

    @Test
    void getId() {
        assertEquals("02-shower", a1.getId());
    }

    @Test
    void getImagePath() {
        assertEquals("/flamethrower.png", a3.getImagePath());
    }

    @Test
    void getTitle() {
        assertEquals("Shower",a1.getTitle());
    }

    @Test
    void getEnergyConsumption() {
        assertEquals(10.2, a1.getEnergyConsumption());
    }

    @Test
    void getSource() {
        assertEquals("example.com", a1.getSource());
    }

    @Test
    void testEquals() {
        assertEquals(a1,a2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(a1,a3);
    }

    @Test
    void testHashCode() {
        assertEquals(a1,a2);
        assertEquals(a1.hashCode(),a2.hashCode());
    }

    @Test
    void notEqualsHashCode() {
        assertNotEquals(a1,a3);
        assertNotEquals(a1.hashCode(),a3.hashCode());
    }

    @Test
    void testToString() {
        String actual = a1.toString();
        assertTrue(actual.contains(Activity.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains(a1.getTitle()));
        assertTrue(actual.contains(a1.getSource()));
        assertTrue(actual.contains(a1.getImagePath()));
    }
}
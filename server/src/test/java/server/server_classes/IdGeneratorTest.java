package server.server_classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    IdGenerator g1;
    @BeforeEach
    void init() {
        g1 = new IdGenerator();
    }

    @Test
    void constructorTest() {
        assertNotNull(g1);
    }

    @Test
    void createID() {
        IdGenerator generator = new IdGenerator();
        assertEquals(0L,generator.createID());
        assertEquals(1L,generator.createID());
        assertEquals(2L,generator.createID());
    }
}
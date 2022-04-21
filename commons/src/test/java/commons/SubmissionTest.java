package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionTest {
    Submission s1;
    Submission s2;
    Submission s3;

    @BeforeEach
    void init() {
        s1 = new Submission("1",7.93);
        s2 = new Submission("1",7.93);
        s3 = new Submission("5493",1.63);
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
    }

    @Test
    void getAnswerVar() {
        assertEquals("1",s1.getAnswerVar());
    }

    @Test
    void getTimerValue() {
        assertEquals(7.93,s1.getTimerValue());
    }

    @Test
    void testEquals() {
        assertEquals(s1,s2);
        assertNotEquals(s1,s3);
    }

    @Test
    void testHashCode() {
        assertEquals(s1.hashCode(),s2.hashCode());
        assertNotEquals(s1.hashCode(),s3.hashCode());
    }

    @Test
    void hasToString() {
        var actual = s1.toString();
        assertTrue(actual.contains(Submission.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("answerVar"));
    }
}
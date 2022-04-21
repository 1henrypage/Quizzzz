package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
    Question q1;
    Question q2;
    Question q3;
    Activity a1;
    Activity a2;
    Activity a3;

    @BeforeEach
    void setUp() {
        a1 = new Activity("02-shower", "/shower.png","Shower", 10.2,"example.com");
        a2 = new Activity("02-shower", "/shower.png","Shower", 10.2,"example.com");
        a3 = new Activity("05-flamethrower", "/flamethrower.png","Flamethrower", 99.3,"example.com");
        q1 = new Question("What is the highest out of the following 3 activities",
                Stream.of(a1,a2,a3).collect(Collectors.toSet()), QuestionType.MC, a1.getId());
        q2 = new Question("What is the highest out of the following 3 activities",
                Stream.of(a1,a2,a3).collect(Collectors.toSet()), QuestionType.MC, a1.getId());
        q3 = new Question("Dummy question", Stream.of(a2).collect(Collectors.toSet()),
                QuestionType.ESTIMATE, Double.toString(a2.getEnergyConsumption()));
    }

    @Test
    void constructorTest() {
        assertNotNull(q1);
        assertEquals("What is the highest out of the following 3 activities",q1.getQuestionText());
        assertEquals(Stream.of(a1,a2,a3).collect(Collectors.toSet()), q1.getActivitySet());
        assertEquals(QuestionType.MC, q1.getType());
    }

    @Test
    void getType() {
        assertEquals(QuestionType.MC, q1.getType());
        assertNotEquals(QuestionType.ESTIMATE, q1.getType());
        assertNotEquals(QuestionType.MC, q3.getType());
        assertEquals(QuestionType.ESTIMATE, q3.getType());
    }

    @Test
    void getQuestionText() {
        assertEquals("What is the highest out of the following 3 activities",q1.getQuestionText());
    }

    @Test
    void getActivities() {
        assertEquals(Stream.of(a1,a2,a3).collect(Collectors.toSet()), q1.getActivitySet());
    }

    @Test
    void testEquals() {
        assertEquals(q1,q2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(q1,q3);
    }

    @Test
    void testHashCode() {
        assertEquals(q1,q2);
        assertEquals(q1.hashCode(),q2.hashCode());
    }

    @Test
    void notEqualsHashCode() {
        assertNotEquals(q1,q3);
        assertNotEquals(q1.hashCode(),q3.hashCode());
    }

    @Test
    void testToString() {
        String actual = q1.toString();
        assertTrue(actual.contains(Question.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("questionText"));
        assertTrue(actual.contains(q1.getQuestionText()));
    }

    @Test
    void getScore() {
        assertEquals(10000, q1.getScore(a1.getId(), 10));
        assertEquals(0, q1.getScore(a3.getId(), 10));
        assertEquals(9000, q1.getScore(a1.getId(), 9));
    }

    @Test
    void getCorrectAnswer() {
        assertEquals(a1.getId(),q1.getCorrectAnswer());
        assertEquals("10.2",q3.getCorrectAnswer());
    }
}
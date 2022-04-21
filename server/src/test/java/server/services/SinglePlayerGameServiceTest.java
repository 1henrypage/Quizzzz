package server.services;

import commons.Question;
import commons.QuestionType;
import commons.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.QuestionGenerator;
import server.database.MockActivityRepository;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.SinglePlayerGame;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SinglePlayerGameServiceTest {

    SinglePlayerGameService s1;
    AbstractGame g1;
    QuestionGenerator questionGenerator;
    Map<Long, AbstractGame> gameMap;
    @BeforeEach
    void init() {
        questionGenerator = new QuestionGenerator(new MockActivityRepository(),new Random(42));
        gameMap = new HashMap<>();
        List<Question> exampleList = List.of(new Question("Example",new HashSet<>(), QuestionType.MC,"3"));
        g1 = new SinglePlayerGame(0L,"Tyrone",exampleList);
        s1 = new SinglePlayerGameService(new IdGenerator(),gameMap,questionGenerator);
    }

    @Test
    void isValidGame() {
        assertFalse(s1.isValidGame(0L));
        s1.createSinglePlayerGame("Kate");
        assertTrue(s1.isValidGame(0L));
    }

    @Test
    void createSinglePlayerGame() {
        assertEquals(0,gameMap.size());
        assertEquals(0L,s1.createSinglePlayerGame("Ey"));
        assertEquals(1,gameMap.size());
    }

    @Test
    void validateAnswer() {
        s1.insertGame(g1);
        Question question = s1.getNextQuestion(0L);
        assertEquals(0,s1.validateAnswer(new Submission("2",7.1),0L));
        assertNotEquals(0,s1.validateAnswer(new Submission("3",7.1),0L));
    }
}
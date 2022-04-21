package server.services;

import commons.Question;
import commons.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.QuestionGenerator;
import server.database.MockActivityRepository;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.SinglePlayerGame;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    AbstractGameService s1;
    AbstractGame g1;
    QuestionGenerator questionGenerator;
    Map<Long, AbstractGame> gameMap;
    @BeforeEach
    void init() {
        questionGenerator = new QuestionGenerator(new MockActivityRepository(),new Random(42));
        gameMap = new HashMap<>();
        List<Question> exampleList = List.of(new Question("Example",new HashSet<>(), QuestionType.MC,"nothing"));
        g1 = new SinglePlayerGame(0L,"Tyrone",exampleList);
        s1 = new SinglePlayerGameService(new IdGenerator(),gameMap,questionGenerator);
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
    }

    @Test
    void createID() {
        assertEquals(0L, s1.createID());
        assertEquals(1, s1.createID());
        assertEquals(2L, s1.createID());
    }

    @Test
    void insertGame() {
        assertEquals(0,gameMap.size());
        s1.insertGame(g1);
        assertEquals(1,gameMap.size());
        assertEquals(g1,gameMap.get(g1.gameID));
    }

    @Test
    void getQuestions() {
        s1.insertGame(g1);
        List<Question> questions = s1.getQuestions(g1.gameID);
        assertEquals(List.of(
                new Question("Example",
                        new HashSet<>(),
                        QuestionType.MC,
                        "nothing")),
                questions);
    }

    @Test
    void getNextQuestion() {
        s1.insertGame(g1);
        assertEquals(new Question("Example",new HashSet<>(), QuestionType.MC,"nothing"),
                s1.getNextQuestion(0L));
        assertNull(s1.getNextQuestion(0L));
    }

    @Test
    void deleteGame() {
        assertEquals(0,gameMap.size());
        s1.insertGame(g1);
        assertEquals(1,gameMap.size());
        assertEquals(g1,gameMap.get(g1.gameID));
        AbstractGame g2 = new SinglePlayerGame(1L,"Rocking",new ArrayList<>());
        s1.insertGame(g2);
        assertEquals(2,gameMap.size());
        assertEquals(0L,s1.deleteGame(0));
        assertEquals(1,gameMap.size());
        assertNotNull(gameMap.get(1L));
    }

    @Test
    void isValidGame() {
        s1.insertGame(g1);
        assertTrue(s1.isValidGame(0L));
        assertFalse(s1.isValidGame(1L));
        s1.deleteGame(0);
        assertFalse(s1.isValidGame(0L));
    }
}
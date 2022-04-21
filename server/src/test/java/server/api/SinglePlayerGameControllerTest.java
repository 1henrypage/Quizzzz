package server.api;

import commons.Activity;
import commons.Question;
import commons.QuestionType;
import commons.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import server.database.MockActivityRepository;
import server.database.MockLeaderboardRepository;
import server.server_classes.*;
import server.services.SinglePlayerGameService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SinglePlayerGameControllerTest {

    @Autowired
    SinglePlayerGameService service;

    SinglePlayerGameController sut;
    Map<Long, AbstractGame> games;
    Set<Activity> testActivitySet;

    @BeforeEach
    void init() {
        Activity a1 = new Activity(
                "1","examplePath",
                "Activity1",23.4,
                "www.exam.com");

        Activity a2 = new Activity(
                "2","examplePath",
                "Activity2",92.5,
                "www.higher.com");

        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        MockActivityRepository mockRepo = new MockActivityRepository();
        mockRepo.saveAll(List.of(a1,a2,a3));
        testActivitySet = Set.of(a3,a2,a1);

        QuestionGenerator mockQuestionGen = new QuestionGenerator(mockRepo,new Random(42));
        this.games = new HashMap<>();
        SinglePlayerGameService service = new SinglePlayerGameService(new IdGenerator(),games,mockQuestionGen);
        sut = new SinglePlayerGameController(service,new MockLeaderboardRepository());
    }

    @Test
    void nullNameTest() {
        var r1 = sut.startSinglePlayer("");
        assertEquals(BAD_REQUEST,r1.getStatusCode());
        var r2 = sut.startSinglePlayer(null);
        assertEquals(BAD_REQUEST,r2.getStatusCode());
    }

    @Test
    void nullIDDelete() {
        var r1 = sut.deleteGame("");
        assertEquals(BAD_REQUEST,r1.getStatusCode());
        var r2 = sut.deleteGame(null);
        assertEquals(BAD_REQUEST,r2.getStatusCode());

    }

    @Test
    void generatingIDTestSingleplayer() {
        sut.startSinglePlayer("Alice");
        sut.startSinglePlayer("Bob");
        var response = sut.startSinglePlayer("Pac");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(2L,response.getBody());
    }

    @Test
    void startSinglePlayer() {
        sut.startSinglePlayer("Hudson");
        assertEquals(1,games.size());
        var game = games.get(0L);
        assertTrue(game instanceof SinglePlayerGame);
        assertEquals(((SinglePlayerGame) game).getPlayerName(),"Hudson");
    }

    @Test
    void deleteGame() {
        sut.startSinglePlayer("Bob");
        assertEquals(1,games.size());
        sut.startSinglePlayer("Alice");
        assertEquals(2,games.size());
        sut.deleteGame("0");
        assertEquals(1,games.size());
        SinglePlayerGame aliceGame = (SinglePlayerGame) games.get(1L);
        assertEquals("Alice",aliceGame.getPlayerName());
    }

    @Test
    void getQuestions() {
        sut.startSinglePlayer("Bob");
        assertEquals(20,sut.getQuestions(String.valueOf(0)).size());
        sut.getNextQuestion(String.valueOf(0));
        assertEquals(19,sut.getQuestions(String.valueOf(0)).size());
        sut.startSinglePlayer("Hannah");
        assertEquals(20,sut.getQuestions(String.valueOf(1)).size());
    }

    @Test
    void getNextQuestion() {
        var r1 = sut.getNextQuestion("0");
        assertEquals(BAD_REQUEST,r1.getStatusCode());
        sut.startSinglePlayer("Hannah");
        var r2 = sut.getNextQuestion("0");
        assertNotNull(r2.getBody());
    }

    @Test
    void validateAnswer() {
        sut.startSinglePlayer("Tyrone");
        List<Question> questionsToBeAsked = sut.getQuestions("0");
        Question firstQuestion = questionsToBeAsked.get(0);
        assertEquals(ResponseEntity.badRequest().build(),
                sut.validateAnswer(new Submission("Test",8.3),"0"));

        Question q1 = sut.getNextQuestion("0").getBody();
        assertEquals(firstQuestion,q1);
        assertEquals(0L,sut.validateAnswer(
                new Submission(
                        firstQuestion.getCorrectAnswer() + 1,2.8),"0").getBody());

        assertEquals(0L,sut.validateAnswer(
                new Submission(
                        firstQuestion.getCorrectAnswer() + 1,10.1),"0").getBody());

        assertNotEquals(0L,sut.validateAnswer(
                new Submission(firstQuestion.getCorrectAnswer(),7.7),"0").getBody());
    }

    @Test
    void testGetQuestions() {
        SinglePlayerGameController fakeSut = new SinglePlayerGameController(
                new SinglePlayerGameService(
                        new IdGenerator(),
                        new HashMap<>(),
                        new QuestionGenerator(new MockActivityRepository(),new Random())
                ), new MockLeaderboardRepository()
        );
        var r1 = fakeSut.startSinglePlayer("Cartoon");
        assertEquals(0L,r1.getBody());
        assertEquals(new ArrayList<Question>(), fakeSut.getQuestions("0"));
        assertNull(fakeSut.getNextQuestion("0").getBody());
        var r2 = sut.startSinglePlayer("Tyrone");
        assertEquals(0L,r2.getBody());
        assertEquals(20,sut.getQuestions("0").size());
        Question sample = new Question(
                "Which one of the following consumes the least energy?",
                testActivitySet, QuestionType.MC,"1"
        );
        assertEquals(sample,sut.getNextQuestion("0").getBody());
    }

    @Test
    void testGetNextQuestion() {
        SinglePlayerGameController fakeSut = new SinglePlayerGameController(
                new SinglePlayerGameService(
                        new IdGenerator(),
                        new HashMap<>(),
                        new QuestionGenerator(new MockActivityRepository(),new Random())
                ), new MockLeaderboardRepository()
        );
        var r1 = fakeSut.startSinglePlayer("Cartoon");
        assertNull(fakeSut.getNextQuestion("0").getBody());
        Question sample = new Question(
                "Which one of the following consumes the least energy?",
                testActivitySet, QuestionType.MC,"1"
        );
        sut.startSinglePlayer("Tyrone");
        sut.startSinglePlayer("Bob");
        Question q1 = sut.getNextQuestion("0").getBody();
        assertNotEquals(q1, sut.getNextQuestion("1").getBody());
        assertEquals(sample,q1);
    }


}
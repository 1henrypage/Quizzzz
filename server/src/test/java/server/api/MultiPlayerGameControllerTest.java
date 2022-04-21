package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.MockActivityRepository;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.MultiPlayerGame;
import server.services.MultiPlayerGameService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MultiPlayerGameControllerTest {


    MultiPlayerGameController sut;
    MultiPlayerGameService service;
    Map<Long, AbstractGame> gameMap;
    Set<Activity> activitySet;
    @BeforeEach
    void init() {
        Activity a1 = new Activity(
                "1","examplePath",
                "Activity1",23.4,
                "www.exam.com");

        Activity a2 = new Activity(
                "2","examplePath",
                "Activity2",50,
                "www.higher.com");

        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        activitySet = Set.of(a1,a2,a3);
        MockActivityRepository repo = new MockActivityRepository();
        repo.saveAll(List.of(a1,a2,a3));
        QuestionGenerator generator = new QuestionGenerator(repo,new Random(42));
        this.gameMap = new HashMap<>();
        service = new MultiPlayerGameService(new IdGenerator(),gameMap,generator);
        sut = new MultiPlayerGameController(service);
    }

    @Test
    void getQuestions() {
        assertNull(sut.getQuestions("0"));
        sut.createMultiplayerGameInternalEndpoint(new ArrayList<>(List.of(
                new PlayerData("H"),
                new PlayerData("P")
        )));
        assertEquals(activitySet,sut.getQuestions("0").get(0).getActivitySet());
    }

    @Test
    void getNextQuestion() {
        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        assertEquals(HttpStatus.BAD_REQUEST,sut.getNextQuestion("0","H").getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,sut.getNextQuestion("1","H").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertNull(sut.getNextQuestion("0", "FDFD").getBody());
        Question question = new Question(
                "Which one of the following consumes the least energy?",
                activitySet, QuestionType.MC,"1");
        Question questionTwo = new Question(
                "How many watt-hours of energy does Activity3 consume?",
                Set.of(a3), QuestionType.ESTIMATE,"24.5"
        );
        assertEquals(question,sut.getNextQuestion("0","H").getBody());
        sut.getNextQuestion("0","H");
        assertEquals(question,sut.getNextQuestion("0","Y").getBody());
    }

    @Test
    void validateAnswer() {
        assertEquals(HttpStatus.BAD_REQUEST,
                sut.validateAnswer(
                        new Submission("fd",32),"0","H").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertEquals(HttpStatus.BAD_REQUEST,sut.validateAnswer(
                new Submission("3",6),"0","H").getStatusCode());
        sut.getNextQuestion("0","H");
        assertEquals(0L,sut.validateAnswer(
                new Submission("1",10.1),"0","H").getBody());
        assertNotEquals(0L,sut.validateAnswer(
                new Submission("1",9.8),"0","H").getBody());
    }

    @Test
    void whoIsCorrect() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.whoIsCorrect("0").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        Map<String,Boolean> sample = new LinkedHashMap<>();
        sample.put("H",false);
        sample.put("Y",false);
        assertEquals(sample,sut.whoIsCorrect("0").getBody());
    }

    @Test
    void informationbox() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.informationbox("HELLO","0").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertEquals("HELLO",sut.informationbox("HELLO","0").getBody());
        assertEquals(List.of("HELLO"),service.getInformationBox(0));
    }

    @Test
    void updater() {
        var r1 = sut.updater("0");
        ResponseEntity<PollWrapper> res = (ResponseEntity<PollWrapper>) r1.getResult();
        assertNotNull(res);
        assertEquals(HttpStatus.BAD_REQUEST,res.getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(new PlayerData("H"),
                new PlayerData("Y")));
        sut.createMultiplayerGameInternalEndpoint(List.of(new PlayerData("P"),
                new PlayerData("Y")));
        var r2 = sut.updater("0");
        assertFalse(r2.hasResult());
        var r3 = sut.updater("1");
        assertFalse(r3.hasResult());
        sut.informationbox("Hello","0");
        assertTrue(r2.hasResult());
        assertFalse(r3.hasResult());
        ResponseEntity<PollWrapper> content = (ResponseEntity<PollWrapper>)
                r2.getResult();
        assertNotNull(content);
        assertEquals(List.of("Hello"), Objects.requireNonNull(content.getBody()).getUiMessages());
        var r4 = sut.updater("0");
        sut.reduceTimeJokerInternalEndpoint(new PlayerData("P"),1);
        assertTrue(r3.hasResult());
        assertFalse(r4.hasResult());
        ResponseEntity<PollWrapper> res2 = (ResponseEntity<PollWrapper>)
                r3.getResult();
        assertNotNull(res2);
        assertNotNull(res2.getBody().getWhoInitiated());
    }

    @Test
    void reduceTimeJokerInternalEndpoint() {
        assertFalse(
                sut.reduceTimeJokerInternalEndpoint
                        (new PlayerData("H"),0));
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertTrue(sut.reduceTimeJokerInternalEndpoint(new PlayerData("H"),0));
    }

    @Test
    void deleteGame() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.deleteGame("0").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertEquals(1,gameMap.size());
        assertEquals(0L,sut.deleteGame("0").getBody());
        assertEquals(0,gameMap.size());

    }

    @Test
    void createMultiplayerGameInternalEndpoint() {
        assertEquals(0,gameMap.size());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("Y"))
        );
        assertEquals(1,gameMap.size());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("L"))
        );
        assertEquals(2,gameMap.size());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("P"))
        );
        assertEquals(3,gameMap.size());
    }

    @Test
    void allScores() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.allScores("0").getStatusCode());
        sut.createMultiplayerGameInternalEndpoint(List.of(
                new PlayerData("H"),
                new PlayerData("L"))
        );
        MultiPlayerGame game = (MultiPlayerGame) gameMap.get(0L);
        Map<String,Long> sample = new HashMap<>();
        sample.put("H",0L);
        sample.put("L",0L);
        assertEquals(sample,sut.allScores("0").getBody());
        game.addScoreFromQuestion(100,"H");
        sample.put("H",100L);
        assertEquals(sample,sut.allScores("0").getBody());
    }
}
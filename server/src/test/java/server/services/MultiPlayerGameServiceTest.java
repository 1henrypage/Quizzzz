package server.services;

import commons.Activity;
import commons.PlayerData;
import commons.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.QuestionGenerator;
import server.database.MockActivityRepository;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.MultiPlayerGame;
import server.server_classes.SinglePlayerGame;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class MultiPlayerGameServiceTest {

    Map<Long, AbstractGame> gameMap;
    MultiPlayerGameService s1;
    SinglePlayerGameService s2; // Testing ID generation between 2 services
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
        IdGenerator generator = new IdGenerator();
        gameMap = new HashMap<>();
        MockActivityRepository mockRepo = new MockActivityRepository();
        mockRepo.saveAll(List.of(a1,a2,a3));
        mockRepo.save(new Activity("9","/flamethrower.png",
                "Flamethrower",77.2,"flamethrower.com"));
        s1 = new MultiPlayerGameService(generator,gameMap,
                new QuestionGenerator(mockRepo,new Random(231412)));
        s2 = new SinglePlayerGameService(generator,gameMap,new QuestionGenerator(mockRepo,new Random(42)));
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
    }

    @Test
    void isValidGame() {
        assertFalse(s1.isValidGame(0L));
        s1.insertGame(new SinglePlayerGame(0L,"Hello",new ArrayList<>()));
        assertFalse(s1.isValidGame(0L));
        s1.insertGame(new MultiPlayerGame(3L,List.of(
                new PlayerData("Hello"),
                new PlayerData("jay")),new ArrayList<>()));
        assertTrue(s1.isValidGame(3L));
    }

    @Test
    void createMultiplayerGame() {
        PlayerData d1 = new PlayerData("Taco");
        PlayerData d2 = new PlayerData("Michael");
        PlayerData d3 = new PlayerData("Barack");
        List<PlayerData> playerDataList = List.of(d1,d2,d3);
        var r1 = s1.createMultiplayerGame(playerDataList);
        assertEquals(0L,r1);
        assertEquals(1,gameMap.size());
        var r2 = s2.createSinglePlayerGame("Test");
        assertEquals(2,gameMap.size());
        assertEquals(1L,r2);
        var r3 = s1.createMultiplayerGame(List.of(d1,d2));
        assertEquals(3,gameMap.size());
    }

    @Test
    void addMessageToInformationBox() {
        assertNull(s1.addMessageToInformationBox(0L,"Hello"));
        s1.createMultiplayerGame(
                List.of(new PlayerData("Taco"),
                        new PlayerData("Pad")));
        assertEquals("Test",s1.addMessageToInformationBox(0L,"Test"));
        assertNull(s1.addMessageToInformationBox(1L,"Test"));
        assertEquals(List.of("Test"),s1.getInformationBox(0L));
    }

    @Test
    void getInformationBox() {
        assertNull(s1.getInformationBox(0L));
        s1.createMultiplayerGame(
                List.of(new PlayerData("Taco"),
                        new PlayerData("Pad")));
        assertEquals(new ArrayList<>(), s1.getInformationBox(0L));
        assertEquals("Test", s1.addMessageToInformationBox(0L, "Test"));
        assertEquals(List.of("Test"), s1.getInformationBox(0L));
    }

    @Test
    void getPlayerScores() {
        PlayerData d1 = new PlayerData("Taco");
        PlayerData d2 = new PlayerData("Michael");
        PlayerData d3 = new PlayerData("Barack");
        s1.createMultiplayerGame(List.of(d1,d2,d3));
        d1.addScore(30L);
        d2.addScore(60L);
        d3.addScore(90L);
        Map<String,Long> sample = new HashMap<>();
        sample.put("Taco",30L);
        sample.put("Michael",60L);
        sample.put("Barack",90L);
        assertEquals(sample,s1.getPlayerScores(0L));
    }

    @Test
    void getNextQuestion() {
        PlayerData d1 = new PlayerData("Taco");
        PlayerData d2 = new PlayerData("Michael");
        PlayerData d3 = new PlayerData("Barack");
        List<PlayerData> playerDataList = List.of(d1,d2,d3);
        assertNull(s1.getNextQuestion(0,"Taco"));
        var r1 = s1.createMultiplayerGame(playerDataList);
        assertEquals(s1.getNextQuestion(0L,"Taco"),
                s1.getNextQuestion(0L,"Michael"));
        assertNotEquals(s1.getNextQuestion(0L,"Barack"),
                s1.getNextQuestion(0L,"Michael"));
        for (int i=0; i < 19; i++) {
            assertNotNull(s1.getNextQuestion(0L,"Taco"));
        }
        assertNull(s1.getNextQuestion(0L,"Taco"));
    }

    @Test
    void getQuestionCorrectnessMap() {
        PlayerData d1 = new PlayerData("Taco");
        PlayerData d2 = new PlayerData("Michael");
        PlayerData d3 = new PlayerData("Barack");
        List<PlayerData> playerDataList = List.of(d1,d2,d3);
        var r1 = s1.createMultiplayerGame(playerDataList);
        Map<String,Boolean> sample = new HashMap<>();
        sample.put("Taco",false);
        sample.put("Michael",false);
        sample.put("Barack",false);
        assertEquals(sample,s1.getQuestionCorrectnessMap(0));
    }

    @Test
    void validateAnswer() {
        PlayerData d1 = new PlayerData("Taco");
        PlayerData d2 = new PlayerData("Michael");
        PlayerData d3 = new PlayerData("Barack");
        List<PlayerData> playerDataList = List.of(d1,d2,d3);
        var r1 = s1.createMultiplayerGame(playerDataList);
        assertEquals(-1L,s1.validateAnswer(
                new Submission("32",7.7),0,"Taco"));
        System.out.println(s1.getNextQuestion(0,"Taco"));
        assertNotEquals(0L,s1.validateAnswer(
                new Submission("23.4",7.7),0,"Taco"));
        Map<String,Boolean> questionCorrectness = new HashMap<>();
        questionCorrectness.put("Taco",true);
        questionCorrectness.put("Michael",false);
        questionCorrectness.put("Barack",false);
        assertEquals(questionCorrectness,s1.getQuestionCorrectnessMap(0));

    }
}
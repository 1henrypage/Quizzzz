package server.api;

import commons.Activity;
import commons.LobbyData;
import commons.PlayerData;
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

class LobbyControllerTest {

    LobbyController sut;
    MultiPlayerGameService service;
    Map<Long, AbstractGame> gameMap;
    PlayerData p1;
    PlayerData p2;

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
        Set<Activity> testActivitySet = Set.of(a3,a2,a1);

        QuestionGenerator mockQuestionGen = new QuestionGenerator(mockRepo,new Random(42));
        this.gameMap = new HashMap<>();
        service = new MultiPlayerGameService(new IdGenerator(),gameMap,mockQuestionGen);
        sut = new LobbyController(service,new MultiPlayerGameController(service));
        p1 = new PlayerData("Mack");
        p2 = new PlayerData("Ruck");
    }

    @Test
    void startGame() {
        assertNull(gameMap.get(0L));
        assertEquals(HttpStatus.BAD_REQUEST,sut.startGame().getStatusCode());
        sut.connect(p1);
        assertEquals(HttpStatus.BAD_REQUEST,sut.startGame().getStatusCode());
        sut.connect(p2);
        var r1 = sut.startGame();
        assertEquals(0L,r1.getBody());
        MultiPlayerGame game = (MultiPlayerGame) gameMap.get(0L);
        assertEquals(List.of("Mack","Ruck"),game.getPlayerNames());
        assertEquals(HttpStatus.BAD_REQUEST,sut.startGame().getStatusCode());
    }

    @Test
    void connect() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.connect(null).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                sut.connect(new PlayerData("")).getStatusCode());

        var r1 = sut.connect(new PlayerData("Mask"));
        assertEquals(HttpStatus.OK,r1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                sut.connect(new PlayerData("Mask"))
                .getStatusCode());
        var r2 = sut.connect(new PlayerData("Krew"));
        List<PlayerData> playerDataList = List.of
                (new PlayerData("Mask"),
                        new PlayerData("Krew"));
        assertEquals(new LobbyData(playerDataList,
                false,-1L),
                r2.getBody());
    }

    @Test
    void disconnect() {
        assertEquals(HttpStatus.BAD_REQUEST,sut.disconnect(null).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,sut.disconnect(new PlayerData("")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                sut.disconnect(new PlayerData("H")).getStatusCode());
        sut.connect(new PlayerData("H"));
        var r1 = sut.disconnect(
                new PlayerData("H"));
        assertEquals(new PlayerData("H"),r1.getBody());
        sut.connect(new PlayerData("H"));
        sut.connect(new PlayerData("D"));
        sut.connect(new PlayerData("P"));
        sut.startGame();
        assertEquals(HttpStatus.BAD_REQUEST,
                sut.disconnect(new PlayerData("H"))
                        .getStatusCode());
        assertEquals(HttpStatus.OK,sut.connect(new PlayerData("H"))
                .getStatusCode());
        sut.connect(new PlayerData("F"));
        sut.connect(new PlayerData("R"));
        assertEquals(new PlayerData("F"),
                sut.disconnect(new PlayerData("F")).getBody());
        sut.startGame();
        List<String> testList = List.of(
                "H","R");
        MultiPlayerGame game = (MultiPlayerGame) gameMap.get(1L);
        assertEquals(testList,game.getPlayerNames());
    }

    @Test
    void playersUpdater() throws InterruptedException {
        var r1 = sut.playersUpdater();
        assertFalse(r1.hasResult());
        sut.connect(new PlayerData("H"));
        assertTrue(r1.hasResult());
        ResponseEntity<LobbyData> test = (ResponseEntity<LobbyData>) r1.getResult();
        assertNotNull(test);
        assertEquals(1, Objects.requireNonNull(test.getBody()).getPlayerDataList().size());
        var r2 = sut.playersUpdater();
        assertFalse(r2.hasResult());
        sut.connect(new PlayerData("P"));
        var r3 = sut.playersUpdater();
        assertEquals(test,r1.getResult());
        assertTrue(r2.hasResult());
        ResponseEntity<LobbyData> testTwo = (ResponseEntity<LobbyData>) r2.getResult();
        assertFalse(r3.hasResult());
        assertNotNull(testTwo);
        assertEquals(2,Objects.requireNonNull(testTwo.getBody()).getPlayerDataList().size());
        assertEquals(new PlayerData("H"),
                sut.disconnect(new PlayerData("H")).getBody());
        assertTrue(r3.hasResult());
        ResponseEntity<LobbyData> testThree = (ResponseEntity<LobbyData>) r3.getResult();
        assertNotNull(testThree);
        assertEquals(List.of(new PlayerData("P")),
                Objects.requireNonNull(testThree.getBody()).getPlayerDataList());
    }
}
package server.api;

import commons.LeaderboardEntry;
import commons.PlayerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import server.database.MockActivityRepository;
import server.database.MockLeaderboardRepository;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.MultiPlayerGame;
import server.services.MultiPlayerGameService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardControllerTest {

    LeaderboardEntry e1;
    LeaderboardEntry e2;
    LeaderboardEntry e3;
    MockLeaderboardRepository repo;
    LeaderboardController sut;
    MockActivityRepository mockActivityRepo;
    MultiPlayerGameService service;
    Map<Long, AbstractGame> gameMap;

    @BeforeEach
    void init() {
        e1 = new LeaderboardEntry("Boris",560L);
        e2 = new LeaderboardEntry("Simon",902L);
        e3 = new LeaderboardEntry("Tyrone",2L);
        repo = new MockLeaderboardRepository();
        mockActivityRepo = new MockActivityRepository();
        repo.saveAll(List.of(e1,e2,e3));

        PlayerData p1 = new PlayerData("A");
        p1.addScore(30L);
        PlayerData p2 = new PlayerData("B");
        p2.addScore(60L);

        gameMap = new HashMap<>();
        gameMap.put(0L,new MultiPlayerGame(0L,List.of(p1,p2),new ArrayList<>()));

        service = new MultiPlayerGameService(new IdGenerator(),
                gameMap,new QuestionGenerator(mockActivityRepo,new Random()));
        sut = new LeaderboardController(repo,service);

    }

    void constructorTest() {
        assertNotNull(sut);
    }


    @Test
    void getAllInOrder() {
        LeaderboardController fakeSUT = new LeaderboardController(
                new MockLeaderboardRepository(),
                service);
        assertEquals(new ArrayList<LeaderboardEntry>(),fakeSUT.getAllInOrder().getBody());
        assertEquals(List.of(e2,e1,e3),sut.getAllInOrder().getBody());
    }

    @Test
    void add() {
        LeaderboardController sutTwo = new LeaderboardController(new MockLeaderboardRepository(),service);
        sutTwo.add(e1);
        assertEquals(1, Objects.requireNonNull(sutTwo.getAllInOrder().getBody()).size());
        assertEquals(e1,sutTwo.getAllInOrder().getBody().get(0));
        sutTwo.add(e2);
        assertEquals(2,sutTwo.getAllInOrder().getBody().size());
        assertEquals(e2,sutTwo.getAllInOrder().getBody().get(0));
        var r1 = sutTwo.add(null);
        assertEquals(HttpStatus.BAD_REQUEST,r1.getStatusCode());
        assertEquals(2,sutTwo.getAllInOrder().getBody().size());
        var r2 = sutTwo.add(new LeaderboardEntry(null,9L));
        assertEquals(HttpStatus.BAD_REQUEST,r2.getStatusCode());
        assertEquals(2,sutTwo.getAllInOrder().getBody().size());
        var r3 = sutTwo.add(new LeaderboardEntry("",3L));
        assertEquals(HttpStatus.BAD_REQUEST,r3.getStatusCode());
        assertEquals(2,sutTwo.getAllInOrder().getBody().size());
    }


    @Test
    void getMultiPlayerScores() {
        LeaderboardController fakeSUT = new LeaderboardController(
                new MockLeaderboardRepository(),
                new MultiPlayerGameService(new IdGenerator(),new HashMap<>(),
                        new QuestionGenerator(mockActivityRepo,new Random())));
        assertEquals(HttpStatus.BAD_REQUEST,fakeSUT.getMultiPlayerScores(0L).getStatusCode());
        Map<String,Long> sample = new HashMap<>();
        sample.put("A",30L);
        sample.put("B",60L);
        assertEquals(sample,sut.getMultiPlayerScores(0L).getBody());
    }
}
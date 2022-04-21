package server.server_classes;

import commons.Activity;
import commons.PlayerData;
import commons.Question;
import commons.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MultiPlayerGameTest {

    AbstractGame s1;
    AbstractGame s2;
    AbstractGame s3;
    MultiPlayerGame sub;
    MultiPlayerGame mainSUB;
    Question q1;
    @BeforeEach
    void init() {
        Activity a1 = new Activity("02-shower", "/shower.png",
                "Shower", 10.2,"example.com");
        Activity a2 = new Activity("02-shower", "/shower.png",
                "Shower", 10.1,"example.com");
        Activity a3 = new Activity("05-flamethrower",
                "/flamethrower.png","Flamethrower", 99.3,"example.com");
        Activity a4 = new Activity("09-heater","/heater.png,",
                "heater",15.9,"heat.com");

        q1 = new Question("Sample q1",
                Stream.of(a1,a2,a3).collect(Collectors.toSet()), QuestionType.MC, a1.getId());

        Question q12 = new Question("Sample",Set.of(a4),
                QuestionType.ESTIMATE,"15.9");

        PlayerData p1 = new PlayerData("Marcus");
        PlayerData p2 = new PlayerData("Kanye");
        PlayerData p3 = new PlayerData("Alice");
        s1 = new MultiPlayerGame(5,List.of(p1,p2,p3),List.of(q1,q1));
        s2 = new MultiPlayerGame(5,List.of(p1,p2,p3), List.of(q1,q1));
        s3 = new MultiPlayerGame(4,List.of(p1,p2,new PlayerData("Alce")),new ArrayList<>());
        sub = (MultiPlayerGame) s1;
        mainSUB = new MultiPlayerGame(9,List.of(p1,p2,p3),List.of(q1,q12));
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
        assertEquals(5,s1.gameID);
        assertEquals(5,s2.gameID);
        assertEquals(4,s3.gameID);
    }

    @Test
    void getPlayerNames() {
        assertEquals(List.of("Marcus","Kanye","Alice"),sub.getPlayerNames());
    }

    @Test
    void getNameScorePairs() {
        Map<String,Long> map = new HashMap<>();
        map.put("Marcus",0L);
        map.put("Kanye",0L);
        map.put("Alice",0L);
        assertEquals(map,sub.getNameScorePairs());
    }

    @Test
    void getPlayerScore() {
        sub.givePoints("Kanye",60);
        assertEquals(60L,sub.getPlayerScore("Kanye"));
    }

    @Test
    void givePoints() {
        sub.givePoints("Alice",779);
        assertEquals(779,sub.getPlayerScore("Alice"));
        sub.givePoints("Kanye",0);
        assertEquals(0,sub.getPlayerScore("Kanye"));
        assertThrows(InvalidParameterException.class, () -> {
            sub.givePoints(null,32);
        });
        assertThrows(InvalidParameterException.class,() -> {
            sub.givePoints("",532);
        });
    }

    @Test
    void addPlayer() {
        assertTrue(sub.addPlayer("Ryan",32));
        assertTrue(sub.getNameScorePairs().containsKey("Ryan"));
        assertEquals(32,sub.getPlayerScore("Ryan"));
        assertEquals(List.of("Marcus","Kanye","Alice","Ryan"),sub.getPlayerNames());
        assertFalse(sub.addPlayer("Kanye",42));
        assertFalse(sub.addPlayer("",58));
        assertFalse(sub.addPlayer(null,569));
    }

    @Test
    void deletePlayer() {
        sub.addPlayer("Ryan",50);
        assertTrue(sub.getNameScorePairs().containsKey("Ryan"));
        assertTrue(sub.deletePlayer("Ryan"));
        assertFalse(sub.getNameScorePairs().containsKey("Ryan"));
        assertEquals(List.of("Marcus","Kanye","Alice"),sub.getPlayerNames());
        assertFalse(sub.deletePlayer("KJDK"));
        assertFalse(sub.deletePlayer(null));
        assertFalse(sub.deletePlayer(""));
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
    public void hasToString() {
        var actual = s1.toString();
        assertTrue(actual.contains(MultiPlayerGame.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("playerDataMap"));
    }

    @Test
    void addMesageToInformationBox() {
        MultiPlayerGame game = (MultiPlayerGame) s1;
        assertEquals(new ArrayList<>(),game.getInformationBox());
        game.addMesageToInformationBox("Henry: Used some Joker!");
        assertEquals(List.of("Henry: Used some Joker!"),game.getInformationBox());
        game.addMesageToInformationBox("System: Player was Disconnected!");
        assertEquals(List.of("Henry: Used some Joker!","System: Player was Disconnected!")
        ,game.getInformationBox());
    }

    @Test
    void getInformationBox() {
        MultiPlayerGame game = (MultiPlayerGame) s1;
        assertEquals(new ArrayList<>(),game.getInformationBox());
        assertEquals("Henry: Used some Joker!",
                game.addMesageToInformationBox("Henry: Used some Joker!"));
        assertEquals(List.of("Henry: Used some Joker!"),game.getInformationBox());
        assertEquals("System: Player was Disconnected!",
                game.addMesageToInformationBox("System: Player was Disconnected!"));
        assertEquals(List.of("Henry: Used some Joker!","System: Player was Disconnected!")
                ,game.getInformationBox());
    }

    @Test
    void getPlayerDataMap() {
        assertNotNull(sub.getPlayerDataMap());
        sub.addPlayer("Henry",32L);
        assertEquals(4,sub.getPlayerDataMap().size());
        assertNotNull(sub.getPlayerDataMap().get("Henry"));
        var r1 = sub.getPlayerDataMap().get("Henry");
        assertNotEquals(new PlayerData("Henry"),r1);
        assertNull(sub.getPlayerDataMap().get("Hello"));
        sub.addPlayer("Hello",0L);
        assertEquals(5,sub.getPlayerDataMap().size());
        assertNotNull(sub.getPlayerDataMap().get("Hello"));
        var r2 = sub.getPlayerDataMap().get("Hello");
        assertEquals(new PlayerData("Hello"),r2);
    }

    @Test
    void getCurrentQuestion() {
        var g = mainSUB;
        assertNull(g.getCurrentQuestion("Kanye"));
        g.getNextQuestion("Kanye");
        g.getNextQuestion("Marcus");
        assertNotNull(g.getCurrentQuestion("Kanye"));
        assertEquals(q1,g.getCurrentQuestion("Kanye"));
        assertEquals(g.getCurrentQuestion("Kanye"),g.getCurrentQuestion("Marcus"));
        g.getNextQuestion("Kanye");
        g.getNextQuestion("Marcus");
        assertEquals(g.getCurrentQuestion("Kanye"),g.getCurrentQuestion("Marcus"));
        g.getNextQuestion("Kanye");
        assertNull(g.getCurrentQuestion("Kanye"));
        g.getNextQuestion("Alice");
        assertNotNull(g.getCurrentQuestion("Alice"));
        assertNotEquals(g.getCurrentQuestion("Kanye"), g.getCurrentQuestion("Alice"));
    }

    @Test
    void getNextQuestion() {
        var g = mainSUB;
        assertEquals(q1,mainSUB.getNextQuestion("Kanye"));
        assertNotNull(mainSUB.getNextQuestion("Kanye"));
        assertNull(mainSUB.getNextQuestion("Kanye"));
        assertNull(mainSUB.getNextQuestion("Kanye"));
        assertEquals(q1,mainSUB.getNextQuestion("Alice"));
    }

    @Test
    void getQuestionCorrectnessMap() {
        var g = mainSUB;
        Map<String,Boolean> sample = new HashMap<>();
        sample.put("Kanye",false);
        sample.put("Marcus",false);
        sample.put("Alice",false);
        Map<String,Boolean> original = new HashMap<>(sample);
        assertEquals(sample,g.getQuestionCorrectnessMap());
        g.getNextQuestion("Alice");
        g.getNextQuestion("Marcus");
        g.getNextQuestion("Kanye");
        g.markAsCorrect("Alice");
        sample.put("Alice",true);
        assertEquals(sample,g.getQuestionCorrectnessMap());
        g.getNextQuestion("Kanye");
        assertEquals(original,g.getQuestionCorrectnessMap());
    }

    @Test
    void useJoker() {
        //TODO
    }

    @Test
    void testAddMesageToInformationBox() {
        var g = mainSUB;
        assertNull(g.addMesageToInformationBox(""));
        assertNull(g.addMesageToInformationBox(null));
        assertEquals("HELLO",g.addMesageToInformationBox("HELLO"));
        assertEquals(List.of("HELLO"),g.getInformationBox());
        assertEquals("WHAT",g.addMesageToInformationBox("WHAT"));
        assertEquals(List.of("HELLO","WHAT"),g.getInformationBox());
        assertEquals(new ArrayList<>(),sub.getInformationBox());
    }

    @Test
    void testGetInformationBox() {
        var g = mainSUB;
        assertNull(g.addMesageToInformationBox(""));
        assertNull(g.addMesageToInformationBox(null));
        assertEquals("HELLO",g.addMesageToInformationBox("HELLO"));
        assertEquals(List.of("HELLO"),g.getInformationBox());
        assertEquals("WHAT",g.addMesageToInformationBox("WHAT"));
        assertEquals(List.of("HELLO","WHAT"),g.getInformationBox());
        assertEquals(new ArrayList<>(),sub.getInformationBox());
    }

    @Test
    void testGetPlayerDataMap() {
        var g = mainSUB;
        Map<String,PlayerData> sample = new HashMap<>();
        sample.put("Kanye",new PlayerData("Kanye"));
        sample.put("Marcus",new PlayerData("Marcus"));
        sample.put("Alice",new PlayerData("Alice"));
        assertEquals(sample,g.getPlayerDataMap());
    }

    @Test
    void addScoreFromQuestion() {
        var g = mainSUB;
        assertEquals(90L,g.addScoreFromQuestion(90L,"Kanye"));
        assertEquals(90L,g.getPlayerScore("Kanye"));
        assertEquals(0L,g.getPlayerScore("Alice"));
    }

    @Test
    void markAsCorrect() {
        var g = mainSUB;
        g.markAsCorrect("Kanye");
        g.markAsCorrect("Marcus");
        Map<String,Boolean> sample = new HashMap<>();
        sample.put("Kanye",true);
        sample.put("Marcus",true);
        sample.put("Alice",false);
        assertEquals(sample,g.getQuestionCorrectnessMap());
        sample.put("Kanye",false);
        sample.put("Marcus",false);
        g.getNextQuestion("Alice");
        assertEquals(sample,g.getQuestionCorrectnessMap());

    }
}
package server.server_classes;

import commons.Activity;
import commons.PlayerData;
import commons.Question;
import commons.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SinglePlayerGameTest {


    AbstractGame s1;
    AbstractGame s2;
    AbstractGame s3;
    SinglePlayerGame sub;
    @BeforeEach
    void init() {
        Activity a1 = new Activity("02-shower", "/shower.png",
                "Shower", 10.2,"example.com");
        Activity a2 = new Activity("02-shower", "/shower.png",
                "Shower", 10.1,"example.com");
        Activity a3 = new Activity("05-flamethrower",
                "/flamethrower.png","Flamethrower", 99.3,"example.com");

        Question question = new Question("Sample question",
                Stream.of(a1,a2,a3).collect(Collectors.toSet()),QuestionType.MC, a1.getId());



        s1 = new SinglePlayerGame(5L,"Marcus", List.of(question));
        s2 = new SinglePlayerGame(5L,"Marcus",List.of(question));
        s3 = new SinglePlayerGame(6L,"Marcu",new ArrayList<>());
        sub = (SinglePlayerGame) s1;
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
        assertEquals(5,s2.gameID);
    }

    @Test
    void getScore() {
        assertEquals(0L,sub.getScore());
    }

    @Test
    void getPlayerName() {
        SinglePlayerGame sub = (SinglePlayerGame) s1;
        assertEquals("Marcus",sub.getPlayerName());
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
        assertTrue(actual.contains(SinglePlayerGame.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("playerName"));
    }

    @Test
    void increaseScore() {
        sub.addScoreFromQuestion(30);
        assertEquals(30,sub.getScore());
    }

    @Test
    void addScoreFromQuestion() {
        var r1 = sub.getNextQuestion();
        sub.addScoreFromQuestion(90);
        assertEquals(90,sub.getPlayerData().getScore());
    }

    @Test
    void getPlayerData() {
        assertEquals(new PlayerData("Marcus"),sub.getPlayerData());
    }
}
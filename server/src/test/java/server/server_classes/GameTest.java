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

class GameTest {

    AbstractGame s1;
    AbstractGame s2;
    @BeforeEach
    void init() {
        Activity a1 = new Activity("02-shower", "/shower.png","Shower",
                10.2,"example.com");
        Activity a2 = new Activity("02-shower", "/shower.png","Shower",
                10.1,"example.com");
        Activity a3 = new Activity("05-flamethrower", "/flamethrower.png",
                "Flamethrower", 99.3,"example.com");

        Question question = new Question("Sample question",
                Stream.of(a1,a2,a3).collect(Collectors.toSet()), QuestionType.MC, a1.getId());


        s1 = new SinglePlayerGame(3,"Bob",List.of(question,question));
        PlayerData p1 = new PlayerData("Bob");
        PlayerData p2 = new PlayerData("Alice");
        s2 = new MultiPlayerGame(4, List.of(p1,p2),new ArrayList<>());
    }

    @Test
    void constructorTest() {
        assertNotNull(s1);
        assertNotNull(s2);
        assertEquals(3,s1.gameID);
        assertEquals(4,s2.gameID);
    }

    @Test
    void getQuestions() {
        assertEquals(new ArrayList<>(),s2.getQuestions());
    }

    @Test
    void getCurrentQuestion() {
        assertNull(s1.getCurrentQuestion());
        Question question = s1.getNextQuestion();
        assertEquals(question,s1.getCurrentQuestion());
    }

    @Test
    void getNextQuestion() {
        assertNull(s2.getCurrentQuestion());
        Question question = s1.getNextQuestion();
        assertEquals(question,s1.getNextQuestion());
    }
}
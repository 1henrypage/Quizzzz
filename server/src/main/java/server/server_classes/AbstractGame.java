package server.server_classes;

import commons.Question;
import java.io.*;
import java.util.*;

public abstract class AbstractGame implements Serializable {
    public final long gameID;
    private List<Question> questions;
    private Question currentQuestion;


    /**
     * Constructor for the game class
     *
     * @param gameID The id for the class
     * @param questions The list of questions to be used in the game
     */
    public AbstractGame(long gameID, List<Question> questions) {
        this.gameID = gameID;
        this.questions = new ArrayList<>(questions);
        this.currentQuestion = null;
    }

    /**
     * Gets all the questions that were assigned to this game.
     *
     * @return A list containing questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * The class variable represents the current question, this method retrieves it
     *
     * @return The question that is currently being asked
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }


    /**
     * Retrieves the next question
     *
     * @return the next question in the list
     */
    public Question getNextQuestion() {
        if (questions.isEmpty()) {
            this.currentQuestion = null;
        } else {
            this.currentQuestion = questions.remove(0);
        }
        return currentQuestion;
    }
    /**
     * Abstract equals method
     *
     * @param obj The object to be compared for equality
     * @return true iff obj is of the same type as this object and has equal attributes, false otherwise
     */
    public abstract boolean equals(Object obj);

    /**
     * Abstract hashCode method
     * generates the hashcode of any child instance of this object
     *
     * @return An integer contianing the hashcode of this object
     */
    public abstract int hashCode();

    /**
     * Abstract toString method
     * generates a human readable representation of any sub-class instance.
     *
     * @return A string containing information about this
     */
    public abstract String toString();
}

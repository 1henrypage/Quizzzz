package server.services;

import commons.Question;
import server.api.QuestionGenerator;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;

import java.util.*;

public abstract class AbstractGameService {

    protected QuestionGenerator questionGenerator;
    protected IdGenerator idGenerator;
    protected final Map<Long, AbstractGame> gameMap;

    /**
     * Constructor for GameService
     *
     * @param idGenerator The idGenerator instance used to generate ids
     * @param gameMap map which maps the game ids to the game object
     * @param questionGenerator container component which is responsible for generating questions
     */
    public AbstractGameService(IdGenerator idGenerator,
                               Map<Long, AbstractGame> gameMap,
                               QuestionGenerator questionGenerator) {
        this.idGenerator = idGenerator;
        this.gameMap = gameMap;
        this.questionGenerator = questionGenerator;
    }

    /**
     * Creates an ID for a new game. Synchronization important here.
     *
     * @return A long containing a new ID.
     */
    protected synchronized long createID() {
        return idGenerator.createID();
    }

    /**
     * Inserts a game into the map of games
     *
     * @param game The game instance to be inserted.
     * @return true, iff it the game was inserted successfully, false otherwise
     */
    public boolean insertGame(AbstractGame game) {
        if (gameMap.get(game.gameID) != null) {
            return false;
        }
        gameMap.put(game.gameID,game);
        return true;
    }


    /**
     * Returns a list of the REMAINING questions in the game
     *
     * @param gameID The ID associated with the game session.
     * @return The list of remaining questions (won't include any removed ones)
     */
    public List<Question> getQuestions(long gameID) {
        return gameMap.get(gameID).getQuestions();
    }

    /**
     * Gets the next question associated with the game instance
     *
     * @param gameID The ID of the game instance
     * @return The next question
     */
    public Question getNextQuestion(long gameID) {
        AbstractGame game = gameMap.get(gameID);
        return game.getNextQuestion();
    }

    /**
     * Deletes a game from the game map.
     *
     * @param gameID The game ID of the game instance to be deleted
     * @return the ID of the game to be delted, -1 if the game was not found.
     */
    public long deleteGame(long gameID) {
        if (gameMap.get(gameID) == null) {
            return -1L;
        }
        gameMap.remove(gameID);
        return gameID;
    }

    /**
     * Abstract method to see if the game instance actually exists.
     *
     * @param gameID The gameID to be checked for existence
     * @return true, iff the game exists, false otherwise
     */
    public abstract boolean isValidGame(long gameID);


    /**
     * Getter for specific game.
     *
     * this may cause ConcurrentException
     *
     * @param gameId the id of that game
     * @return that specific game
     */
    public AbstractGame getGame(long gameId) throws IllegalArgumentException{
        if(!isValidGame(gameId)){
            throw new IllegalArgumentException();
        }

        return gameMap.get(gameId);
    }
}

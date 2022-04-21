package server.services;

import commons.Question;
import commons.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.api.QuestionGenerator;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;
import server.server_classes.SinglePlayerGame;

import java.util.*;

@Service
public class SinglePlayerGameService extends AbstractGameService {

    /**
     * Constructor for SinglePlayerGameService
     *
     * @param idGenerator The instance of idGenerator that will be used
     * @param gameMap The map game to be used.
     * @param questionGenerator The container component which is responsible for generating questions
     */
    @Autowired
    public SinglePlayerGameService(IdGenerator idGenerator,
                                   Map<Long, AbstractGame> gameMap,
                                   QuestionGenerator questionGenerator) {
        super(idGenerator,gameMap, questionGenerator);
    }

    /**
     * Checks if it is a valid SinglePlayerGame instance
     *
     * @param gameID The gameID to be checked for existence
     * @return true, iff there exists a game that is Singleplayer, false otherwise
     */
    @Override
    public boolean isValidGame(long gameID) {
        return gameMap.get(gameID) != null &&
                gameMap.get(gameID) instanceof SinglePlayerGame;
    }

    /**
     * Creates a singleplayer game
     *
     * @param name Name associated with this game instance
     * @return The ID of the game
     */
    public long createSinglePlayerGame(String name) {
        List<Question> pregenQuestions = questionGenerator.generate20Questions();
        SinglePlayerGame game;
        if (pregenQuestions.get(0) == null) {
            game = new SinglePlayerGame(createID(),name,new ArrayList<>());
        } else {
            game = new SinglePlayerGame(createID(),name,pregenQuestions);
        }
        gameMap.put(game.gameID,game);
        return game.gameID;
    }

    /**
     * Validates a user's answer and gives points accordingly
     *
     * @param answerPair The submission object used to hold the answer string and the
     *                   timer at that time.
     * @param gameID The ID of the game session.
     * @return The updated score, the scame score will be returned if the user submitted the wrong answer.
     */
    public long validateAnswer(Submission answerPair,long gameID) {
        AbstractGame currentGame = gameMap.get(gameID);
        if (currentGame.getCurrentQuestion()==null) {
            return -1L;
        }
        long incScore = currentGame.getCurrentQuestion().getScore(answerPair.getAnswerVar(),answerPair.getTimerValue());
        SinglePlayerGame curGame = (SinglePlayerGame) gameMap.get(gameID);
        return curGame.addScoreFromQuestion(incScore);
    }
}

package server.api;

import commons.LeaderboardEntry;
import commons.Question;
import commons.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import server.database.LeaderboardRepository;
import server.server_classes.SinglePlayerGame;
import server.services.SinglePlayerGameService;


@RestController
@RequestMapping("/api/game/singleplayer")
public class SinglePlayerGameController {

    private final SinglePlayerGameService service;
    private final LeaderboardRepository leaderBoardRepo;

    /**
     * Constructor for GameController
     *
     * @param service injected component which corresponds to the business logic of this controller
     * @param leaderBoardRepo The leaderboard repository where entries will be added
     *                        after the completion of games
     */
    @Autowired
    public SinglePlayerGameController(SinglePlayerGameService service,
                                      LeaderboardRepository leaderBoardRepo) {
        this.service = service;
        this.leaderBoardRepo = leaderBoardRepo;
    }

    /**
     * API endpoint for starting a single-player game
     *
     * All the user has to provide for a single-player game is their username.
     * The API will respond with the ID of the newly created game (game session).
     *
     * @param name The name of the user
     * @return the id of the newly created game. (now referred to as game session)
     */
    @PostMapping(path="/create") // "/api/game/singleplayer/create"
    public ResponseEntity<Long> startSinglePlayer(@RequestBody String name) {
        if (name == null || name.length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.createSinglePlayerGame(name));
    }

    /**
     * Returns a list of REMAINING questions in the game
     *
     * @param id The id associated with the game session.
     * @return The list of remaining questions (won't include already removed ones)
     */
    @GetMapping(path="questions/{id}") // "/api/game/singleplayer/questions/{id}"
    public List<Question> getQuestions(@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (id.length() == 0 || !service.isValidGame(gameID)) {
            return null;
        }
        return service.getQuestions(gameID);
    }

    /**
     * API endpoint to get the next question
     *
     * @param id The id of the game session
     * @return A ResponseEntity containing the next question. A null value if there is no next exception
     */
    @GetMapping(path ="getquestion/{id}") // "/api/game/singleplayer/getquestion/{id}"
    public ResponseEntity<Question> getNextQuestion(@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (id.length() == 0 || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        Question nextQuestion = service.getNextQuestion(gameID);
        if (nextQuestion == null) {
            SinglePlayerGame game = (SinglePlayerGame) service.getGame(gameID);
            leaderBoardRepo.save(
                    new LeaderboardEntry(game.getPlayerName(),game.getPlayerData().getScore())
            );
        }
        return ResponseEntity.ok(nextQuestion);
    }

    /**
     * Validates the answer the user provided, and adds points based on whether the user got it correct.
     *
     * @param answerPair A submission object which contains both the answer of the user (which varies according
     *                   to the question type) and the time left on the timer at the time of submission.
     *                   These are the two question types:
     *                   MCQ: Should contain the ID of the activity that the user chose
     *                   Estimate: Should contain the energy usage that the user inputted
     * @param id the ID of the game session
     * @return A ResponseEntity containing the current score of the game, will return the same score
     * that the user previously had if the user submitted the wrong answer.
     */
    @PostMapping(path="validate/{id}") // "/api/game/singleplayer/validate/{id}"
    public ResponseEntity<Long> validateAnswer(@RequestBody Submission answerPair, @PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (answerPair == null || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        long r1 = service.validateAnswer(answerPair,gameID);
        if (r1 != -1L) {
            return ResponseEntity.ok(r1);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * API endpoint for deleting a game
     *
     * @param id The id of the game. Should not be {@literal null}
     * @return A ResponseEntity containing the ID of the game.
     */
    @GetMapping(path="/delete/{id}") // "/api/game/singleplayer/delete/{id}"
    public ResponseEntity<Long> deleteGame(@PathVariable("id") String id) {
        if (id==null || id.length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        long gameID = Long.parseLong(id);
        long result = service.deleteGame(gameID);
        if (result != -1L) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().build();
    }
}

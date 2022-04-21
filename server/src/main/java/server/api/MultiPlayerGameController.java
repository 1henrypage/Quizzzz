package server.api;

import commons.PlayerData;
import commons.PollWrapper;
import commons.Question;
import commons.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.MultiPlayerGameService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/game/multiplayer")
public class MultiPlayerGameController {

    private final MultiPlayerGameService service;
    private Map<Long,Map<Object, Consumer<PollWrapper>>> listeners = new ConcurrentHashMap<>();

    /**
     * Constructor for the MultiplayerGameController
     * 
     * @param service The service used to handle all business logic
     */
    @Autowired
    public MultiPlayerGameController(MultiPlayerGameService service) {
        this.service = service;
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
     * API endpoint to get the next question, for the individual player
     *
     * @param id The id of the game session
     * @param name The name of the player within the respective game
     * @return A ResponseEntity containing the next question. A null value if there is no next question
     */
    @GetMapping(path ="getquestion/{id}/{name}")
    public ResponseEntity<Question> getNextQuestion(@PathVariable("id") String id,
                                                    @PathVariable("name") String name) {
        long gameID = Long.parseLong(id);
        if (id.length() == 0 || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getNextQuestion(gameID,name));
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
     * @param name name of the player within the multiplayer game session
     * @return A ResponseEntity containing the current score of the game, will return the same score
     * that the user previously had if the user submitted the wrong answer.
     */
    @PostMapping(path="validate/{id}/{name}") // "/api/game/singleplayer/validate/{id}"
    public ResponseEntity<Long> validateAnswer(@RequestBody Submission answerPair,
                                               @PathVariable("id") String id,
                                               @PathVariable("name") String name) {
        long gameID = Long.parseLong(id);
        if (answerPair == null || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        long r1 = service.validateAnswer(answerPair,gameID,name);
        if (r1 != -1L) {
            return ResponseEntity.ok(r1);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * API endpoint to see who is correct with regards to the current question.
     * WARNING: There is no guarantee to the order of key-value pairs, when iterating over them.
     *
     * @param id The id of the current game
     * @return A map containg key-value pairs of who is correct and who is wrong
     */
    @GetMapping(path="whoiscorrect/{id}")
    public ResponseEntity<Map<String,Boolean>> whoIsCorrect(@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (!service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }

        var map = service.getQuestionCorrectnessMap(gameID);
        return ResponseEntity.ok(map);
    }

    /**
     * API endpoint which maps the names of people in the current game instance,
     * to their numerical score.
     *
     * @param id The ID of the game to get the score from.
     * @return The map which contains the name and score.
     */
    @GetMapping(path="scores/{id}")
    public ResponseEntity<Map<String,Long>> allScores(@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (!service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }

        var map = service.getPlayerScores(gameID);
        return ResponseEntity.ok(map);
    }

    /**
     * API endpoint to be used when client wants to send something into the information box.
     * 2 Scenarios:
     *      - The client has used a joker, and this needs to be announced in the box.
     *      - The client has used a reaction and this needs to be announced.
     *
     * @param id The ID of the MultiPlayer game instance
     * @param message The message that should be added to the UI Box
     * @return A ResponseEntity containing the message that was just added.
     */
    @PostMapping(path="/informationbox/{id}")
    public ResponseEntity<String> informationbox(@RequestBody String message,@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);
        if (id.length() == 0 || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        service.addMessageToInformationBox(gameID,message);
        Map<Object,Consumer<PollWrapper>> gameListener = listeners.get(gameID);
        if (gameListener != null) {
            gameListener.forEach((k,l) -> l.accept(
                    new PollWrapper(service.getInformationBox(gameID), null)));
        }
        return ResponseEntity.ok(message);
    }

    /**
     * LONG POLLING ENDPOINT: Used for communicating updates of the server to the client
     *
     * @param id The ID of the game
     * @return The DeferredResult which will receive a MultiPlayerPollObject,
     * when new information is available.
     */
    @GetMapping(path="/update/{id}")
    public DeferredResult<ResponseEntity<PollWrapper>> updater(@PathVariable("id") String id) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<PollWrapper>>(5000L,noContent);

        long gameID = Long.parseLong(id);
        if (id.length() == 0 || !service.isValidGame(gameID)) {
            res.setErrorResult(ResponseEntity.badRequest().build());
            return res;
        }

        var key = new Object();
        listeners.get(gameID).put(key, pollObject -> {
            res.setResult(ResponseEntity.ok(pollObject));
        });
        res.onCompletion(() -> {
            listeners.get(gameID).remove(key);
        });
        return res;
    }

    /**
     * Internal Endpoint to be called from the lobby controller to start a game
     *
     * @param playerDataList The list of players associated with this multiplayer game instance
     * @return The gameID of the newly created multiplayer game
     */
    @PostMapping(path="/create")
    public long createMultiplayerGameInternalEndpoint(@RequestBody List<PlayerData> playerDataList) {
        long gameID = service.createMultiplayerGame(playerDataList);
        listeners.put(gameID,new ConcurrentHashMap<>());
        return gameID;
    }

    /**
     * To be used by classes who inject this controller (like the joker class), to communicate
     * to every other client that someone has pressed the reduce time joker
     *
     * @param playerData The player who initiated the joker
     * @param gameID The game ID of the game
     * @return true if it was executed successfully, false otherwise.
     */
    public boolean reduceTimeJokerInternalEndpoint(PlayerData playerData, long gameID) {
        if (!service.isValidGame(gameID)) {
            return false;
        }
        Map<Object,Consumer<PollWrapper>> gameListener = listeners.get(gameID);
        if (gameListener != null) {
            gameListener.forEach((k,l) ->
                    l.accept(new PollWrapper(null, playerData)));
        }
        return true;
    }

    /**
     * API endpoint for deleting a game
     *
     * @param id The id of the game. Should not be {@literal null}
     * @return A ResponseEntity containing the ID of the game.
     */
    @GetMapping(path="/delete/{id}") // "/api/game/multiplayer/delete/{id}"
    public ResponseEntity<Long> deleteGame(@PathVariable("id") String id) {
        long gameID = Long.parseLong(id);

        if (id.length() == 0 || !service.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        long result = service.deleteGame(gameID);
        if (result != -1L) {
            listeners.remove(gameID);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().build();
    }

}

package server.api;

import commons.PlayerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.JokerUse;
import commons.JokerType;
import server.server_classes.MultiPlayerGame;
import server.server_classes.SinglePlayerGame;
import server.services.MultiPlayerGameService;
import server.services.SinglePlayerGameService;

@RestController
@RequestMapping("/api/joker/")
public class JokerController {
    private SinglePlayerGameService singlePlayerGameService;
    private MultiPlayerGameService multiPlayerGameService;
    private MultiPlayerGameController multiPlayerGameController;

    /**
     * Constructor for JokerController.
     * @param singlePlayerGameService the singleplayer game service
     * @param multiPlayerGameService the multiplayer game service
     * @param multiPlayerGameController the multiplayer game controller
     */
    @Autowired
    public JokerController(
            SinglePlayerGameService singlePlayerGameService,
            MultiPlayerGameService multiPlayerGameService,
            MultiPlayerGameController multiPlayerGameController) {
        this.singlePlayerGameService = singlePlayerGameService;
        this.multiPlayerGameService = multiPlayerGameService;
        this.multiPlayerGameController = multiPlayerGameController;
    }

    /**
     * API endpoint to get the next question
     *
     * @param jokerUse the JokerUse object with the request
     * @return A ResponseEntity containing the next question. A Bad Request if there is no next question
     */
    @PostMapping(path ="multiplayer/use/")
    public ResponseEntity<String> useJokerMultiplayer (@RequestBody JokerUse jokerUse) {
        JokerType jokerType = jokerUse.getJokerType();
        MultiPlayerGame game;
        String playerName = jokerUse.getPlayerName();

        try {
            game = (MultiPlayerGame) multiPlayerGameService.getGame(jokerUse.getGameId());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("GameId not valid", HttpStatus.BAD_REQUEST);
        }
        if (!game.getPlayerNames().contains(playerName)){
            return new ResponseEntity<>("Player does not exist", HttpStatus.BAD_REQUEST);
        }
        if (!game.useJoker(playerName, jokerType)){
            return new ResponseEntity<>("Joker already used", HttpStatus.FORBIDDEN);
        }
        if(jokerType.equals(JokerType.REDUCE_TIME)){
            PlayerData userData = game.getPlayerDataMap().get(playerName);
            for(PlayerData data: game.getPlayerDataMap().values()){
                if(!userData.equals(data)) {
                    multiPlayerGameController.reduceTimeJokerInternalEndpoint(
                            game.getPlayerDataMap().get(playerName), jokerUse.getGameId());
                }
            }
            userData.markJokerAsUsed(jokerType);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * API endpoint to use a joker
     *
     * @param jokerUse the JokerUse object with the request
     * @return HttpStatus according to the result of the joker use
     */
    @PostMapping(path ="singleplayer/use")
    public ResponseEntity<String> useJokerSingleplayer (@RequestBody JokerUse jokerUse) {

        JokerType jokerType = jokerUse.getJokerType();
        SinglePlayerGame game;

        try {
            game = (SinglePlayerGame) singlePlayerGameService.getGame(jokerUse.getGameId());
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("GameId not valid", HttpStatus.BAD_REQUEST);
        }
        if (game == null) {
            return new ResponseEntity<>("GameId not valid", HttpStatus.BAD_REQUEST);
        }

        if(!game.useJoker(jokerType)){
            return new ResponseEntity<>("Joker already used", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}

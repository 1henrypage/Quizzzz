package server.api;

import commons.LobbyData;
import commons.PlayerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.MultiPlayerGameService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private MultiPlayerGameController controller;
    private final MultiPlayerGameService service;
    private Map<Object, Consumer<LobbyData>> playerListeners = new ConcurrentHashMap<>();
    private List<PlayerData> connectedPlayers = new ArrayList<>();

    /**
     * Constructor for WaitingRoomController
     *
     * @param service The MultiplayerGameService used to handle all business logic of MultiPlayerGames
     * @param controller The MultiPlayerGameController to delegate the creation of the game.
     */
    @Autowired
    public LobbyController(MultiPlayerGameService service, MultiPlayerGameController controller) {
        this.service = service;
        this.controller = controller;
    }

    /**
     * Method to be called, when a client presses on the start game button.
     *
     * @return The game ID which was assigned to the set of players.
     */
    @GetMapping(path="/start")
    public ResponseEntity<Long> startGame() {
        if (connectedPlayers.size() < 2) {
            return ResponseEntity.badRequest().build();
        }
        long gameID = controller
                .createMultiplayerGameInternalEndpoint(connectedPlayers);
        playerListeners.forEach((k,l) ->
                l.accept(new LobbyData(connectedPlayers,true,gameID)));
        connectedPlayers.clear();
        playerListeners.clear();
        return ResponseEntity.ok(gameID);
    }

    /**
     * Method to connect to the current waiting room.
     *
     * @param data The playerData object which corresponds to the client.
     * @return A ResponseEntity containing a LobbyData object,
     * which represents the current state of the lobby
     */
    @PostMapping(path="/connect")
    public ResponseEntity<LobbyData> connect(@RequestBody PlayerData data) {
        if (!checkValidPlayerData(data) || nameAlreadyExists(data)) {
            return ResponseEntity.badRequest().build();
        }
        connectedPlayers.add(data);
        playerListeners.forEach((k,l) ->
                l.accept(new LobbyData(connectedPlayers,false,-1L)));
        return ResponseEntity.ok(new LobbyData(connectedPlayers,false,-1L));
    }

    /**
     * API endpoint corresponding to when the client presses on the "back" button
     *
     * @param data The PlayerData object of the client disconnecting
     * @return A ResponseEntity containing the PlayerData object that was removed.
     */
    @PostMapping(path="/disconnect")
    public ResponseEntity<PlayerData> disconnect(@RequestBody PlayerData data) {
        if (!checkValidPlayerData(data) || !nameAlreadyExists(data)) {
            return ResponseEntity.badRequest().build();
        }
        connectedPlayers.remove(data);
        playerListeners.forEach((k,l) ->
                l.accept(new LobbyData(connectedPlayers,false,-1L)));
        return ResponseEntity.ok(data);
    }

    /**
     * LONG POLLING ENDPOINT: Used for communicating updates of the waiting room to the client
     *
     * @return A DeferredResult containing a ResponseEntity with the LobbyData
     */
    @GetMapping(path="/update")
    public DeferredResult<ResponseEntity<LobbyData>> playersUpdater() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<LobbyData>>(5000L,noContent);

        var key = new Object();
        playerListeners.put(key, lobbyData -> {
            res.setResult(ResponseEntity.ok(lobbyData));
        });
        res.onCompletion(() -> {
            playerListeners.remove(key);
        });
        return res;
    }

    /**
     * Checks if the name already exists in this lobby
     *
     * @param data The PlayerData object to be checked for name equality.
     * @return true iff, there is some other PlayerData object with the same name.
     */
    private boolean nameAlreadyExists(PlayerData data) {
        return connectedPlayers
                .stream()
                .map(PlayerData::getPlayerName)
                .anyMatch(x -> Objects.equals(data.getPlayerName(),x));
    }

    /**
     * Checks validity for the PlayerData object
     *
     * @param data The PlayerData object that should be checked for validity
     * @return true iff if it is valid, false otherwise
     */
    private boolean checkValidPlayerData(PlayerData data) {
        return data != null
                && data.getPlayerName() != null
                && data.getPlayerName().length() > 0;
    }

}

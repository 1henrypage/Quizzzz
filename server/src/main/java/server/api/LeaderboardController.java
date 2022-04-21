package server.api;

import java.util.*;

import commons.LeaderboardEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.LeaderboardRepository;
import server.services.MultiPlayerGameService;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardRepository repo;
    private final MultiPlayerGameService multiPlayerGameService;

    /**
     * LeaderboardController constructor
     *
     * @param repo The repository containing the entries of the leaderboard
     * @param multiPlayerGameService The service handling game logic for multiplayer games
     */
    @Autowired
    public LeaderboardController(LeaderboardRepository repo,
                                 MultiPlayerGameService multiPlayerGameService) {
        this.repo = repo;
        this.multiPlayerGameService = multiPlayerGameService;
    }

    /**
     * Returns every single LeaderboardEntry currently in repo
     * as a list (sorted in descending order)
     *
     * @return A list which contains LeaderBoardEntries, sorted
     * according to their score.
     */
    @GetMapping(path = {"","/"})
    public ResponseEntity<List<LeaderboardEntry>> getAllInOrder() {
        List<LeaderboardEntry> sortedList = repo.findAllByOrderByScoreDesc();
        return ResponseEntity.ok(sortedList);
    }

    /**
     * API endpoint to save entries to the DB
     * @param entry The entry to be saved.
     * @return A responseentity containing the entry if it was successful,
     * a bad request otherwise.
     */
    @PostMapping(path={"/add","/add/"})
    public ResponseEntity<LeaderboardEntry> add(
            @RequestBody LeaderboardEntry entry) {
        if (entry == null || isNullOrEmpty(entry.getName())) {
            return ResponseEntity.badRequest().build();
        }

        LeaderboardEntry saved = repo.save(entry);
        return ResponseEntity.ok(saved);
    }

    /**
     * API endpoint to request a map of names and scores associated to a multiplayer game
     *
     * @param gameID The ID of the multiplayer game instance
     * @return A Map containing names and scores of a certain game
     */
    @GetMapping(path="/multiplayer/{id}")
    public ResponseEntity<Map<String,Long>> getMultiPlayerScores(@PathVariable("id") long gameID) {
        if (!multiPlayerGameService.isValidGame(gameID)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(multiPlayerGameService.getPlayerScores(gameID));
    }

    /**
     * Checks if a string is null or empty
     *
     * @param s string to be checked for conditions
     * @return true iff, it's s is not null and not empty, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

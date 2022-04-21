/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;

import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

public class ServerUtils {

    private static String SERVER = "http://localhost:8080/";
    private static ExecutorService EXEC;
    private static ExecutorService EXEC2;
    private String gameID;

    /**
     * Sets the IP of the desired server
     *
     * @param ip The IP of the server
     * @return The newly set Server-IP
     */
    public String setIP(String ip) throws ConnectException {
        SERVER = ip;
        try {
            testEndpoint();
        } catch (Exception e) {
            throw new ConnectException();
        }
        return SERVER;
    }

    /**
     * Sample Endpoint to ping, before a user actually enters any sort of game interface/
     * admin interface
     *
     * @return The string from the server which makes sure that this is a valid IP
     */
    private String testEndpoint() throws ConnectException {
        var r1 = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
        if (!Objects.equals(r1,"1kxIEPWKIFKzjHFnnZYPHD43KFMGOP")) {
            throw new ConnectException();
        }
        return r1;
    }

    /**
     *
     * @return returns all the activities
     */
    public List<Activity> getActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/admin/activities")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }

    /**
     *
     * @param path
     * @param image
     * @return boolean
     */
    public boolean uploadImage(String path, byte[] image) {
        //all / in path are replaced by [+] to work with the url
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/admin/uploadimage/" + path.replace("/", "[+]"))
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(image, APPLICATION_JSON), Boolean.class);
    }

    /**
     *
     * @param activity
     * @return returns all the activities
     */
    public Activity saveActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/admin/addactivity")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     *
     * @param id of the activity
     * @return returns all the activities
     */
    public String deleteActivity(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/admin/deleteactivity/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }

    /**
     *
     * @return returns 20 questions
     */
    public Question getQuestions() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/singleplayer/questions/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Question.class);
    }

    /**
     * Gets the next question in the question set
     * @return Adding checkstyle
     * @param gameID
     */
    public Question getQuestion(String gameID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/singleplayer/getquestion/"+ gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Question.class);
    }

    /**
     * Gets the next question in the question set
     * @return Adding checkstyle
     * @param gameID
     * @param name
     */
    public Question getMPQuestion(String gameID, String name) {
        this.gameID = gameID;
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/multiplayer/getquestion/"+ gameID + "/" + name)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Question.class);
    }

    /**
     * Gets the leaderboard entries sorted in order
     * @return adding checkstyle
     */
    public List<LeaderboardEntry> getLeaderboardEntries(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/leaderboard/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<LeaderboardEntry>>() {});
    }

    /**
     * Adds a leaderboard entry to the repo
     * @param leaderboardEntry
     * @return adding checkstyle
     */
    public LeaderboardEntry addLeaderboardEntry(LeaderboardEntry leaderboardEntry){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/leaderboard/add/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(leaderboardEntry, APPLICATION_JSON), LeaderboardEntry.class);
    }

    /**
     * @param gameID
     * @param submission
     * @return validates an answer and returns the updated score
     */
    public Long validateQuestion(Submission submission,String gameID){
         return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/singleplayer/validate/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(submission, APPLICATION_JSON), Long.class);
    }

    /**
     *
     * @param submission
     * @param gameID
     * @param name
     * @return validates an answer and returns the updated score
     */
    public Long validateMPQuestion(Submission submission,String gameID, String name){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/multiplayer/validate/" + gameID + "/" + name)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(submission, APPLICATION_JSON), Long.class);
    }


    /**
     *
     * @param name
     * @return returns and ID of the newly created game
     */
    public String createGame(String name){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/singleplayer/create/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(name, APPLICATION_JSON), String.class);
    }

    /**
     *
     * @param data
     * @return new LobbyData object representing the current state of the lobby
     */
    public LobbyData connect(PlayerData data){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lobby/connect")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(data, APPLICATION_JSON), LobbyData.class);
    }

    /**
     *
     * @param data
     * @return PlayerData object that was removed
     */
    public PlayerData disconnect(PlayerData data){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lobby/disconnect")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(data, APPLICATION_JSON), PlayerData.class);
    }

    /**
     *
     * @return game ID assigned to the set of players
     */
    public Long startLobby(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lobby/start")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Long.class);
    }

    /**
     *
     * @param consumer
     */
    public void longPollingLobby(Consumer<LobbyData> consumer){
        EXEC = Executors.newSingleThreadExecutor();
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
               var res =  ClientBuilder.newClient(new ClientConfig())
                        .target(SERVER).path("api/lobby/update")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
               if(res.getStatus() == 204){
                   continue;
               }
               var l = res.readEntity(LobbyData.class);
               consumer.accept(l);
            }
        });
    }

    /**
     *
     * @param consumer
     */
    public void longPollingMP(Consumer<PollWrapper> consumer){
        EXEC2 = Executors.newSingleThreadExecutor();
        EXEC2.submit(() -> {
            while(!Thread.interrupted()) {
                var res =  ClientBuilder.newClient(new ClientConfig())
                        .target(SERVER).path("/api/game/multiplayer/update/" + gameID)
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                if(res.getStatus() == 204){
                    continue;
                }
                var data = res.readEntity(PollWrapper.class);
                consumer.accept(data);
            }
        });
    }

    /**
     *
     */
    public void stopLobbyLP(){
        if (EXEC != null) {
            EXEC.shutdownNow();
        }
    }

    /**
     *
     */
    public void stopMainLP(){
        if (EXEC2 != null) {
            EXEC2.shutdownNow();
        }
    }

    /**
     *
     * @param gameID
     * @param emoji
     * @return String object that was removed
     */
    public String sendToInformationBox(String gameID, String emoji){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/multiplayer/informationbox/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(emoji, APPLICATION_JSON), String.class);
    }

    /**
     *
     * @param id
     * @return map of players and their scores
     */
    public Map<String,Long> getPlayerScores(String id){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/multiplayer/scores/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }



    /** Sends a message to the server to use a joker in singleplayer
     * @param gameId The id of the game session
     * @param jokerType The type of joker it is
     * @return true iff joker has been use successfully
     */
    public boolean useJokerSingleplayer(long gameId, JokerType jokerType) {
        JokerUse use = new JokerUse(gameId, jokerType);

        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/joker/singleplayer/use/")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(use, APPLICATION_JSON), JokerUse.class);
        } catch (ForbiddenException | BadRequestException e) {
            return false;
        }
        return true;
    }

    /** Sends a message to the server to use a joker in singleplayer
     * @param gameId The id of the game session
     * @param playerName The name of the player
     * @param jokerType The type of joker it is
     * @return true iff joker has been use successfully
     */
    public boolean useJokerMultiplayer(long gameId,String playerName, JokerType jokerType) {
        JokerUse use = new JokerUse(gameId, playerName, jokerType);

        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/joker/multiplayer/use/")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(use, APPLICATION_JSON), JokerUse.class);
        } catch (ForbiddenException | BadRequestException e) {
            throw e;
            //return false;
        }
        return true;
    }

    /**
     * Gets all the jokers for a game (Will change if there are more jokers in the future)
     * @param gameId
     * @return jokers for the game
     */
    public List<JokerType> getJokers(String gameId) {
        //TODO: figure out the endpoint in case of more jokers
        List<JokerType> list = new ArrayList<JokerType>();
        list.add(JokerType.DOUBLE_POINTS);
        list.add(JokerType.REDUCE_TIME);
        list.add(JokerType.REMOVE_WRONG_ANSWER);
        return list;
    }
}
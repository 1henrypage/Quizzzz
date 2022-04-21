package server.server_classes;

import commons.JokerType;
import commons.PlayerData;
import commons.Question;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.security.InvalidParameterException;
import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class MultiPlayerGame extends AbstractGame {
    private final Map<String, PlayerData> playerDataMap;
    private final Map<String,Queue<Question>> questionQueueMap;
    private final Map<String,Question> currentQuestionMap;
    private final Map<String,Boolean> questionCorrectnessMap;
    private final List<String> informationBox = new ArrayList<>();

    /**
     * Constructor for a MultiPlayerGame
     *
     * @param gameID The ID of the game
     * @param playerDataList A list containing all the player data objects of this game
     * @param questions The list of 20 pre-generated questions to be used in this game instance.
     */
    public MultiPlayerGame(long gameID, List<PlayerData> playerDataList, List<Question> questions) {
        super(gameID, questions);
        this.questionCorrectnessMap = new LinkedHashMap<>();
        playerDataList.forEach(data -> questionCorrectnessMap.put(data.getPlayerName(),false));
        this.playerDataMap = new LinkedHashMap<>();
        playerDataList.forEach(data -> playerDataMap.put(data.getPlayerName(),data));
        this.questionQueueMap = new LinkedHashMap<>();
        playerDataList.forEach(data -> questionQueueMap.put(data.getPlayerName(),
                new LinkedList<>(questions)));
        this.currentQuestionMap = new LinkedHashMap<>();
        playerDataList.forEach(data -> currentQuestionMap.put(data.getPlayerName()
                ,null));
    }

    /**
     * Gets the currentQuestion for the PlayerData specified here.
     *
     * @param name The name of the player that needs the question retrieved.
     * @return The question that is currently being asked
     */
    public Question getCurrentQuestion(String name) {
        return currentQuestionMap.get(name);
    }

    /**
     * Gets the next question FOR THE PLAYER with respect to the current game.
     *
     * @param name The name of the player
     * @return The next question
     */
    public Question getNextQuestion(String name) {
        if (questionQueueMap.get(name) == null) {
            return null;
        }

        if (questionQueueMap.get(name).isEmpty()) {
            currentQuestionMap.put(name,null);
        } else {
            currentQuestionMap.put(name,questionQueueMap.get(name).poll());
            this.questionCorrectnessMap.replaceAll((key,value) -> false);
        }
        return currentQuestionMap.get(name);
    }

    /**
     * Returns the names of the players associated to this MultiPlayerGame instance
     *
     * @return The names of players in this game
     */
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        playerDataMap.forEach((k,l) -> names.add(k));
        return names;
    }

    /**
     * Gets the players and their scores in this multiplayer instance
     *
     * @return A map which contains key value pairs pertaining to names and scores
     */
    public Map<String, Long> getNameScorePairs() {
        Map<String, Long> nameScorePairs = new HashMap<>();

        playerDataMap.forEach((name, data) -> nameScorePairs.put(name, data.getScore()));

        return nameScorePairs;
    }

    /**
     * Gets the score of the player in this multiplayer instance
     *
     * @param name The name of the player
     * @return The score of the player in this multiplayer instance
     * @throws InvalidParameterException if the player is not in the lobby or the name violates
     * naming conventions
     */
    public long getPlayerScore(String name) {
        if (name==null || name.length() == 0 || playerDataMap.get(name) == null) {
            throw new InvalidParameterException();
        } else {
            return playerDataMap.get(name).getScore();
        }
    }


    /**
     * Gives the player points by increasing the corresponding value in the
     * map.
     *
     * @param name The name of the player
     * @param points The number of points you want to give the player
     * @return The new number of points the player has (old value + new value)
     * @throws InvalidParameterException if the name violates some existence properties
     */
    public long givePoints(String name, long points) {
        if (name==null || name.length() == 0 || playerDataMap.get(name)==null) {
            throw new InvalidParameterException();
        }

        playerDataMap.get(name).addScore(points);
        return playerDataMap.get(name).getScore();
    }

    /**
     * Getter for the question correctness map: Maps names to whether people got the
     * current question right or not
     *
     * @return The question correctness map
     */
    public Map<String, Boolean> getQuestionCorrectnessMap() {
        return questionCorrectnessMap;
    }

    /**
     * Adds a player to the game.
     * This method is just for extensibility procedures, probably won't be used
     *
     * @param name The name of the player to be added
     * @param startingPoints The number of points the player has to start, will usually be 0
     * @return true iff the player was added successfully (not already existing in game) false otherwise
     */
    @Deprecated // This method doesn't correcly synchronize question quees with other people do not actually
    //use it outside the contexts of tests.
    public boolean addPlayer(String name, long startingPoints) {
        if (Objects.equals(null,name) || name.length() == 0 || playerDataMap.containsKey(name)) {
            return false;
        }

        playerDataMap.put(name, new PlayerData(name));
        playerDataMap.get(name).addScore(startingPoints);
        return true;
    }


    /**
     * Deletes a player from the game (backend).
     *
     * @param name The name of the player to be deleted
     * @return true if the player was successfully deleted, false otherwise
     */
    public boolean deletePlayer(String name) {
        if (Objects.equals(null,name) || name.length() == 0 || !playerDataMap.containsKey(name)) {
            return false;
        }

        playerDataMap.remove(name);
        currentQuestionMap.remove(name);
        questionCorrectnessMap.remove(name);
        questionQueueMap.remove(name);
        return true;
    }

    /**
     * Compares two objects based on equality
     *
     * @param obj The object to be tested for equality
     * @return true iff, o is an instanceof MultiplayerGame and has equivalent attributes, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Generates a hashcode for the object
     *
     * @return An integer containing the hashcode of this object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Generates a string representation of this object
     *
     * @return A string which represents this instance of a MultiplayerGame
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * Uses the joker.
     *
     * @param playerName the name of the player
     * @param jokerType the type of joker he uses
     * @return true iff the joker was successfully used
     */
    public boolean useJoker(String playerName, JokerType jokerType) {
        return playerDataMap.get(playerName).useJoker(jokerType);
    }

    /**
     * Adds a message to the information box
     *
     * @param message The message to be added
     * @return The message itself.
     */
    public String addMesageToInformationBox(String message) {
        if (message==null || message.length() == 0) {
            return null;
        }
        informationBox.add(message);
        return message;
    }

    /**
     * Getter for the messages in the Information Box
     *
     * @return The list of strings corresponding to messages in the information box
     */
    public List<String> getInformationBox() {
        return informationBox;
    }

    /**
     * Getter for playerData
     *
     * @return the playerData
     */
    public Map<String, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    /**
     * Method to increase the score of the player in the current game session
     * Joker calculations are also made.
     *
     * @param inc A long representing how much you want to increase the score by.
     * @param name The person of whos score to increase
     * @return The (new) cumulative score
     */
    public long addScoreFromQuestion(long inc, String name){
        PlayerData playerData = playerDataMap.get(name);
        if(playerData.needsToBeExecuted(JokerType.DOUBLE_POINTS)){
            inc *= 2;
            playerData.markJokerAsUsed(JokerType.DOUBLE_POINTS);
        }

        playerData.addScore(inc);
        return playerData.getScore();
    }

    /**
     * The player is marked as getting the question correct in the map.
     * @param name The name of the player
     */
    public void markAsCorrect(String name) {
        questionCorrectnessMap.put(name,true);
    }
}

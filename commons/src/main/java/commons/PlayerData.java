package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class PlayerData {

    public enum JokerUsageType{
        NOT_USED,
        TO_BE_EXECUTED,
        USED
    }

    private long score;
    private String playerName;
    private Map<JokerType, JokerUsageType> jokers;

    /**
     * Constructor for PlayerData.
     * @param playerName the name of the player
     */
    public PlayerData(String playerName) {
        Set<JokerType> jokerSet = Set.of(
                JokerType.DOUBLE_POINTS,
                JokerType.REDUCE_TIME,
                JokerType.REMOVE_WRONG_ANSWER);
        this.score = 0;

        this.jokers = new HashMap<>();

        jokerSet.forEach(joker -> this.jokers.put(joker, JokerUsageType.NOT_USED));

        this.playerName = playerName;
    }

    /**
     * Private constructor for jackson deserialization
     */
    private PlayerData(){}

    /**
     * Getter for playerName
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Adds to the player's score.
     *
     * @param amount the amount to increase the score by
     * @return The UPDATED cumulative score. Will be the same score if the amount is 0
     */
    public long addScore(long amount){
        score += amount;
        return score;
    }

    /**
     * Getter for score
     *
     * @return the score
     */
    public long getScore(){
        return score;
    }

    /**
     * Getter for jokers field
     * @return map of jokers
     */
    public Map<JokerType, JokerUsageType> getJokers() {
        return jokers;
    }

    /**
     * Setter for jokers field
     * @param jokers
     */
    public void setJokers(Map<JokerType, JokerUsageType> jokers) {
        this.jokers = jokers;
    }

    /**
     * Checks if the joker has been used.
     *
     * @param joker the joker to be checked
     * @return true iff joker has been used
     */
    public boolean jokerHasBeenUsed(JokerType joker){
        return jokers.get(joker) == JokerUsageType.USED;
    }


    /**
     * this marks a joker as used (completely)
     * @param jokerType the type of joker to be marked
     */
    public void markJokerAsUsed(JokerType jokerType) {
        jokers.put(jokerType, JokerUsageType.USED);
    }

    /**
     * Marks the joker as used
     *  //TODO: actually use the joker to change the game state. WIP
     *
     * @param joker the joker to be used.
     * @return true iff the use was successful
     */
    public boolean useJoker(JokerType joker){
        if (jokerHasBeenUsed(joker)) {
            return false;
        }

        jokers.put(joker, JokerUsageType.TO_BE_EXECUTED);
        return true;
    }

    /**
     * This is a bit like the equals method, but it only tests for the same name
     * The reason why this was implemented, is that the server and client might modify the jokers
     * or the player score. This means that they won't be equal anymore.
     *
     * @param data The object to be tested for the same name
     * @return true iff obj is an instanceof PlayerData and has the same name, false otherwise
     */
    public boolean hasSameName(PlayerData data) {
        if (this == data) {
            return true;
        }

        if (data != null) {
            return Objects.equals(this.playerName, data.playerName);
        }
        return false;
    }

    /**
     * Check if a joker is marked as TO_BE_EXECUTED
     *
     * @param joker the joker to be checked
     * @return true iff the joker is to be executed
     */
    public boolean needsToBeExecuted(JokerType joker){
        return jokers.get(joker) == JokerUsageType.TO_BE_EXECUTED;
    }

    /**
     * Compares two objects based on equality
     *
     * @param obj The object to be tested for equality
     * @return true iff, o is an instanceof PlayerData and has equivalent attributes, false otherwise
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
     * @return A string which represents this instance of PlayerData
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}

package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * @author hpage - Ask if confused
 *
 * This class is a wrapper class for the communication between the client and server
 * during the waiting room.
 */
public class LobbyData {

    private List<PlayerData> playerDataList;
    private boolean inStartState;
    private long assignedGameID;

    /**
     * Object mapper
     */
    private LobbyData() {

    }

    /**
     * Constructor for LobbyData
     *
     * @param playerDataList The list of PlayerData items that correspond
     *                       to the current state of the waiting room/lobby
     * @param isInStartState true if the game is in the start state. This will indicate
     *                       when one of the clients
     * @param assignedGameID The game ID of the game instance assigned to this lobby, once
     *                       the game is in it's start state.
     */
    public LobbyData(List<PlayerData> playerDataList, boolean isInStartState,long assignedGameID) {
        this.playerDataList = playerDataList;
        this.inStartState = isInStartState;
        this.assignedGameID = assignedGameID;
    }

    /**
     * Gets the associated PlayerData objects in this waiting room/lobby
     *
     * @return A list containing playerData objects
     */
    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    /**
     * Determines whether the game is in a start state.
     * @return The
     */
    public boolean isInStartState() {
        return inStartState;
    }

    /**
     * The getter for the ID of the game that was assigned to this instance,
     * if the game hasn't started yet it returns -1.
     *
     * @return The game ID associated to this instance
     */
    public long getAssignedGameID() {
        if (!inStartState) {
            return -1L;
        }
        return assignedGameID;
    }

    /**
     * enhanced equals method
     *
     * @param o The object to be compared to
     * @return true if the two Objects have tested equals.
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * enhanced hashcode method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * enhanced toString
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}

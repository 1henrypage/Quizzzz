package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class JokerUse implements Serializable {
    private long gameId;
    private String playerName;
    private JokerType jokerType;

    /**
     * for object mapper
     */
    private JokerUse(){}

    /**
     * Constructor for JokerUse
     * @param gameId the id of the game
     * @param jokerType the type of joker
     */
    public JokerUse(long gameId, JokerType jokerType) {
        this.gameId = gameId;
        this.jokerType = Objects.requireNonNull(jokerType, "must not be null");
    }

    /**
     *
     * Constructor for JokerUse
     * @param gameId the id of the game
     * @param playerName the name of the player
     * @param jokerType the type of joker
     */
    public JokerUse(long gameId, String playerName, JokerType jokerType) {
        this.gameId = gameId;
        this.jokerType = Objects.requireNonNull(jokerType, "must not be null");
        this.playerName = Objects.requireNonNull(playerName, "must not be null");
    }

    /**
     * Getter for gameId
     * @return the gameId
     */
    public long getGameId() {
        return gameId;
    }

    /**
     * Getter for playerName
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter for jokerType
     * @return the jokerType
     */
    public JokerType getJokerType() {
        return jokerType;
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

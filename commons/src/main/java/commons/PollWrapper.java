package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class PollWrapper {

    private List<String> uiMessages;
    private PlayerData whoInitiated;

    /**
     * For Object mapper
     */
    private PollWrapper() {}

    /**
     * Constructor for PollWrapper
     * @param uiMessages
     * @param whoInitiated
     */
    public PollWrapper(List<String> uiMessages, PlayerData whoInitiated) {
        this.uiMessages = uiMessages;
        this.whoInitiated = whoInitiated;
    }

    /**
     * Getter for uiMessages
     * @return uiMessages
     */
    public List<String> getUiMessages() {
        return uiMessages;
    }

    /**
     * Setter for uiMessages
     * @param uiMessages
     */
    public void setUiMessages(List<String> uiMessages) {
        this.uiMessages = uiMessages;
    }

    /**
     * Getter for whoInitiated
     * @return whoInitiated
     */
    public PlayerData getWhoInitiated() {
        return whoInitiated;
    }

    /**
     * Setter for whoInitiated
     * @param whoInitiated
     */
    public void setWhoInitiated(PlayerData whoInitiated) {
        this.whoInitiated = whoInitiated;
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

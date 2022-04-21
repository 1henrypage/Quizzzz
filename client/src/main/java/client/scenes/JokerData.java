package client.scenes;

import commons.JokerType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class JokerData {
    private final String text;
    private final JokerType type;
    private boolean used;
    private final boolean mc;
    private final boolean estimate;
    private final boolean sp;
    private final boolean mp;

    /**
     * Constructor for JokerData
     *
     * @param text  displayed on UI
     * @param type  (JokerType)
     * @param used
     * @param mc    MultipleChoice
     * @param estimate
     * @param sp    Single-player
     * @param mp    Multi-player
     */
    public JokerData(String text, JokerType type,
                     boolean used, boolean mc, boolean estimate, boolean sp, boolean mp) {
        this.text = text;
        this.type = type;
        this.used = used;
        this.mc = mc;
        this.estimate = estimate;
        this.sp = sp;
        this.mp = mp;
    }

    /**
     * Getter for mc
     *
     * @return the boolean
     */
    public boolean isMc() {
        return mc;
    }

    /**
     * Getter for estimate
     *
     * @return the boolean
     */
    public boolean isEstimate() {
        return estimate;
    }

    /**
     * Getter for sp
     *
     * @return the boolean
     */
    public boolean isSp() {
        return sp;
    }

    /**
     * Getter for mp
     *
     * @return the boolean
     */
    public boolean isMp() {
        return mp;
    }

    /**
     * Setter for used
     *
     * @param used the used
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Getter for used
     *
     * @return the boolean
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public JokerType getType() {
        return type;
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

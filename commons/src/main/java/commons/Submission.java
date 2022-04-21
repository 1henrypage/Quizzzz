package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Submission implements Serializable {
    private String answerVar;
    private double timerValue;

    /**
     * constructor for Submission
     *
     * @param answerVar Depending on what type of question it is, this will represent different things.
     *                  MCQ: The ID of the activity corresponding to the user-submitted answer
     *                  ESTIMATE: The estimate that the user gave
     * @param timerValue The time shown on the timer on the client
     */
    public Submission(String answerVar, double timerValue) {
        this.answerVar = Objects.requireNonNull(answerVar, "must not be null");
        this.timerValue = Objects.requireNonNull(timerValue, "must not be null");
    }

    /**
     * Object mapper
     */
    private Submission(){}


    /**
     * gets the answerVar (what the user answered) and returns it
     *
     * @return the answerVar associated with the current question
     */
    public String getAnswerVar() {
        return answerVar;
    }

    /**
     * The number the timer was displaying at the time of the submission
     *
     * @return The time left on the timer
     */
    public double getTimerValue() {
        return timerValue;
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

package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Question {

    private String questionText;
    private Set<Activity> activitySet;
    private QuestionType type;
    private String correctAnswer;

    /**
     * default constructor for object mapper
     */
    private Question() {
        // for object mapper
    }

    /**
     * constructor for Question
     *
     * @param questionText the text of the question
     * @param activitySet the activities for the question
     * @param type the type of question
     * @param correctAnswer the correct answer to the question
     */
    public Question(String questionText, Set<Activity> activitySet, QuestionType type, String correctAnswer) {
        this.questionText = questionText;
        this.activitySet = activitySet;
        this.type = type;
        this.correctAnswer = correctAnswer;
    }

    /**
     * returns the score a player gets for a question
     *
     * @param answer the answer given by a player
     * @param time the time left to answer the question
     * @return the score he gets for the answer
     */
    public long getScore(String answer, double time){
        if(time <= 0 || time > 10) {
            return 0;
        }
        if(type.equals(QuestionType.MC) || type.equals(QuestionType.SELECTIVE)){
            if (answer.equals(correctAnswer)) {
                return (long) (time * 1000);
            }
            return 0;
        }
        if(type.equals(QuestionType.ESTIMATE)){
            double answerDouble = Double.parseDouble(answer);
            double correctAnswerDouble = Double.parseDouble(correctAnswer);

            double answerRatio = Math.abs(answerDouble / correctAnswerDouble - 1);
            if(answerRatio > 1) {
                return 0;
            }
            answerRatio = 1 - answerRatio;

            return (long) (answerRatio * time * 1000);
        }

        return  0;
    }

    /**
     * type getter
     *
     * @return the type of question this is
     */
    public QuestionType getType(){
        return type;
    }


    /**
     * returns the textual representation of the question
     *
     * @return A String containing the question
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * activitySet getter
     *
     * @return A set containing all the activities
     */
    public Set<Activity> getActivitySet()     {
        return activitySet;
    }

    /**
     * correctAnswer getter
     *
     * @return A string indicating the correct answer
     */
    public String getCorrectAnswer(){
        return correctAnswer;
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
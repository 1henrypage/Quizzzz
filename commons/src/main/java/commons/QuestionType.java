package commons;

public enum QuestionType {
    MC,
    SELECTIVE,
    ESTIMATE;

    /**
     * This method tells us how many activities a question type requires.
     * @param type the type of question
     * @return the amount of activities required for said question
     */
    static public int getAmountOfActivities(QuestionType type) {
        return switch (type) {
            case MC, SELECTIVE -> 3;
            case ESTIMATE -> 1;
        };
    }
}

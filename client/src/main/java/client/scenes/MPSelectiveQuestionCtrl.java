package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.JokerType;
import commons.Question;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MPSelectiveQuestionCtrl extends AbstractMPQuestionCtrl {

    @FXML
    private Button answerText1;

    @FXML
    private Button answerText2;

    @FXML
    private Button answerText3;

    @FXML
    private Button jokerButton0;

    @FXML
    private Button jokerButton1;

    @FXML
    private Button jokerButton2;

    @FXML
    private ImageView image;


    private Question associatedQuestion;
    private ArrayList<Button> buttonList;

    /**
     * Constructor for SPSelectiveQuestionCtrl
     * @param server that can communicate with backend
     * @param myMainCtrl
     */
    @Inject
    public MPSelectiveQuestionCtrl(ServerUtils server, MyMainCtrl myMainCtrl) {
        super(server, myMainCtrl);
    }

    /**
     * Gets called upon init
     * @param question
     * @param score
     * @param infoList
     * @param list
     */
    public void init(Question question, Long score, ObservableList<String> list, ObservableList<String> infoList){
        buttonList = new ArrayList<>(Arrays.asList(answerText1, answerText2, answerText3));
        jokerList = new ArrayList<>(Arrays.asList(jokerButton0, jokerButton1, jokerButton2));
        init(score, list, infoList);
        associatedQuestion = question;

        questionText.setText(question.getQuestionText());

        question.getActivitySet().forEach(a -> {
            if (a.getId().equals(question.getCorrectAnswer())) {
                var imageBytes = a.image;
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                Image i = new Image(bis);
                if(i.isError()){
                    image.setImage(new Image("file:client/src/main/resources/client/scenes/Energy-Placeholder.png"));
                }else{
                    image.setImage(i);
                }
            }
        });
        setUpUI(question);
    }

    void setUpUI(Question q) {
        var activityIterator = q.getActivitySet().iterator();
        answerText1.setText(activityIterator.next().getEnergyConsumption() + "Wh");
        answerText2.setText(activityIterator.next().getEnergyConsumption() + "Wh");
        answerText3.setText(activityIterator.next().getEnergyConsumption() + "Wh");

        activityIterator = q.getActivitySet().iterator();
        answerText1.setId(activityIterator.next().getId());
        answerText2.setId(activityIterator.next().getId());
        answerText3.setId(activityIterator.next().getId());

        answerText1.setFocusTraversable(false);
        answerText2.setFocusTraversable(false);
        answerText3.setFocusTraversable(false);
    }

    @Override
    protected void goToLeaderboard(long score, ObservableList<String> list, ObservableList<String> infoList) {
        enableButtons(true);
        enableColors(buttonList);
        resetUI();
        Platform.runLater(() -> myMainCtrl.showMPhalfTimeLeaderboardScreen(score, list, infoList));
        answerTimerTask.cancel();
    }



    /**
     * Event handler for pressing an answer button
     * @param event
     */
    @FXML
    void answerPress(ActionEvent event) {
        Button source = (Button) event.getSource();
        processAnswer(source.getId());
        enableButtons(false);
    }

    @Override
    protected void goToNextScene(long score) {

    }

    /**
     * Method that resets all the UI elements to their base state
     */
    @Override
    protected void resetUI() {
        super.resetUI();
        image.setImage(null);
    }


    /**
     * Method that submits an empty answer with time 0 to the server
     */
    @Override
    public void timeOut() {
        updateColors(associatedQuestion.getCorrectAnswer());
        super.timeOut();
    }

    @Override
    protected void disableControls() {
        super.disableControls();
        enableButtons(false);
    }

    @Override
    protected void goToNextScene(long score, ObservableList<String> list) {

    }

    /**
     *
     * @param score
     * @param list
     * @param infoList
     */
    @Override
    protected void goToNextScene(long score, ObservableList<String> list, ObservableList<String> infoList) {
        enableButtons(true);
        enableColors(buttonList);
        resetUI();
        Platform.runLater(() -> myMainCtrl.setNextMPQuestion(score, list, infoList));
        answerTimerTask.cancel();
    }

    /**
     * Method that returns the button colors back to normal
     * @param buttonList
     */
    public void enableColors(ArrayList<Button> buttonList){
        for (Button b : buttonList) {
            b.getStyleClass().removeAll("questionButtonCorrect");
            b.getStyleClass().removeAll("questionButtonIncorrect");
        }
    }

    /**
     * Method that enables the answer buttons
     * @param flag
     */
    public void enableButtons(boolean flag){

        for (Button b : buttonList) {
            b.setDisable(!flag);
        }
    }

    /**
     *  Method that shows the incorrect and correct answers
     * @param correctAnswer
     */
    public void updateColors(String correctAnswer) {
        for (Button b : buttonList) {
            String answer = b.getId();
            if (answer.equals(correctAnswer)) {
                b.getStyleClass().add("questionButtonCorrect");
            } else {
                b.getStyleClass().add("questionButtonIncorrect");
            }
        }
    }

    @Override
    protected void setUpJokers() {
        int i = 0;
        jokerMap = new HashMap<>();
        for (JokerData joker : myMainCtrl.getJokerList()) {
            if (joker != null && joker.isMp() && joker.isMc()) {
                jokerMap.put("jokerButton" + i, joker);
                jokerList.get(i).setDisable(joker.isUsed());
                jokerList.get(i).setText(joker.getText());
                jokerList.get(i).setFocusTraversable(false);

                i++;
            }
        }
    }

    @Override
    protected JokerType jokerPress(ActionEvent event) {
        JokerType type = super.jokerPress(event);

        if (type == JokerType.REMOVE_WRONG_ANSWER) {
            for (Button b : buttonList) {
                String answer = b.getId();
                if (!answer.equals(associatedQuestion.getCorrectAnswer())) {
                    b.getStyleClass().add("questionButtonIncorrect");
                    b.setDisable(true);
                    return type;
                }
            }
        }
        return type;
    }
}

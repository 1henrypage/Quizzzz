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
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MPMultipleChoiceQuestionCtrl extends AbstractMPQuestionCtrl {

    @FXML
    private Button activityButton1;

    @FXML
    private Button activityButton2;

    @FXML
    private Button activityButton3;

    @FXML
    private Text activityText1;

    @FXML
    private Text activityText2;

    @FXML
    private Text activityText3;

    @FXML
    private Button jokerButton0;

    @FXML
    private Button jokerButton1;

    @FXML
    private Button jokerButton2;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    private Question associatedQuestion;
    private ArrayList<Button> buttonList;

    /**
     * Constructor for QuestionController
     *
     * @param server   that can communicate with backend
     * @param myMainCtrl
     */
    @Inject
    public MPMultipleChoiceQuestionCtrl(ServerUtils server, MyMainCtrl myMainCtrl) {
        super(server, myMainCtrl);
    }

    /**
     * Gets called upon init
     * @param question
     * @param score
     * @param list
     * @param infoList
     */
    public void init(Question question, Long score, ObservableList<String> list, ObservableList<String> infoList) {
        buttonList = new ArrayList<>(Arrays.asList(activityButton1, activityButton2, activityButton3));
        jokerList = new ArrayList<>(Arrays.asList(jokerButton0, jokerButton1, jokerButton2));
        init(score, list, infoList);
        associatedQuestion = question;
        questionText.setText(question.getQuestionText());

        var activityIterator = question.getActivitySet().iterator();
        activityText1.setText(activityIterator.next().getTitle());
        activityText2.setText(activityIterator.next().getTitle());
        activityText3.setText(activityIterator.next().getTitle());

        activityIterator = question.getActivitySet().iterator();
        activityButton1.setId(activityIterator.next().getId());
        activityButton2.setId(activityIterator.next().getId());
        activityButton3.setId(activityIterator.next().getId());

        activityButton1.setFocusTraversable(false);
        activityButton2.setFocusTraversable(false);
        activityButton3.setFocusTraversable(false);

        setPictures(associatedQuestion);
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



    /**
     * Method that transitions from the current question to the next one
     * @param score
     */
    @Override
    protected void goToNextScene(long score) {

    }

    @FXML
    void setPictures(Question question){
        var activityIterator = question.getActivitySet().iterator();

        for(int i=1;i<=3;i++) {
            var bytes = activityIterator.next().image;
            boolean error = false;
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            Image img = new Image(bis);
            if(img.isError()){
                error = true;
            }
            checkAndSet(error,i,img);
        }
    }

    /**
     *
     * @param error
     * @param index
     * @param img
     */
    public void checkAndSet(boolean error, int index, Image img){
        if(index == 1){
            if(error){
                image1.setImage(new Image("file:client/src/main/resources/client/scenes/Energy-Placeholder.png"));
            }else{
                image1.setImage(img);
            }
        }
        if(index == 2){
            if(error){
                image2.setImage(new Image("file:client/src/main/resources/client/scenes/Energy-Placeholder.png"));
                System.out.println("Error");
            }else{
                image2.setImage(img);
            }
        }
        if(index == 3){
            if(error){
                image3.setImage(new Image("file:client/src/main/resources/client/scenes/Energy-Placeholder.png"));
            }else{
                image3.setImage(img);
            }
        }
    }

    /**
     * Method that submits an empty answer with time 0 to the server
     */
    @Override
    public void timeOut() {
        updateColors(buttonList, associatedQuestion.getCorrectAnswer());
        super.timeOut();
    }

    @Override
    protected void disableControls() {
        super.disableControls();
        enableButtons(false);
    }

    @Override
    protected void goToNextScene(long score, ObservableList<String> list, ObservableList<String> infoList) {
        enableButtons(true);
        enableColors(buttonList);
        resetUI();
        Platform.runLater(() -> myMainCtrl.setNextMPQuestion(score, list, infoList));
        answerTimerTask.cancel();
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
     * @param buttonList
     * @param correctAnswer
     */
    public void updateColors(ArrayList<Button> buttonList, String correctAnswer) {
        for (Button b : buttonList) {
            String answer = b.getId();
            b.setDisable(true);
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

    /**
     * Don't use, used for SP
     * @param score
     * @param list
     */
    @Override
    protected void goToNextScene(long score, ObservableList<String> list) {

    }
}

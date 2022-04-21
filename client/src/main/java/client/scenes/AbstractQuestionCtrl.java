package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.JokerType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractQuestionCtrl extends AbstractCtrl {

    @FXML
    protected Text questionText;

    @FXML
    protected Text scoreText;

    @FXML
    protected ProgressBar timerBar;

    @FXML
    protected Text timerText;

    protected final ServerUtils server;
    protected final MyMainCtrl myMainCtrl;

    protected Timer mainTimer;
    protected TimerTask mainTimerTask;
    protected Timer answerTimer;
    protected TimerTask answerTimerTask;

    protected HashMap<String, JokerData> jokerMap;
    protected ArrayList<Button> jokerList;

    @FXML
    private Label counter;

    /**
     * Constructor for QuestionController
     *
     * @param server that can communicate with backend
     * @param mainCtrl
     */
    @Inject
    public AbstractQuestionCtrl(ServerUtils server, MyMainCtrl mainCtrl) {
        this.server = server;
        this.myMainCtrl = mainCtrl;
    }

    /**
     * Gets called upon init
     *
     * @param score the score to be initialized with the question
     */
    public void init(Long score) {
        resetUI();
        setUpJokers();
        scoreText.setText("Score: " + score);
        timerText.setText("10s");
        timerBar.setProgress(10);

        counter.setText("Q: " + String.valueOf(myMainCtrl.questionCounter) + "/20");
        timer();
    }

    /**
     * Method that takes care of the UI timer functionality
     */
    public void timer() {
        mainTimer = new Timer();
        mainTimerTask = new TimerTask() {
            double progressTime = 10;
            int timer = 100;

            @Override
            public void run() {
                Platform.runLater(() -> timerBar.setProgress(progressTime / 10));
                if (timer == 100) {
                    changeTimerTime(progressTime);
                    timer = 0;
                }
                progressTime = progressTime - 0.01;
                timer++;
                if (progressTime < 0) {
                    Platform.runLater(() -> timerText.setText(0 + " s"));
                    this.cancel();
                    Platform.runLater(() -> timeOut());
                }
            }
        };
        this.mainTimer.schedule(mainTimerTask, 0, 10);
    }

    protected void changeTimerTime(double progressTime) {
        var textTime = (int) progressTime;
        Platform.runLater(() -> timerText.setText(textTime + " s"));
        Platform.runLater(() -> changeColor(textTime));
    }

    /**
     * Cancels the current timer and returns the progress of the current question
     *
     * @return the current timer value
     */
    public Double cancelTimer() {
        Double result = timerBar.getProgress();
        timerBar.setProgress(10);
        mainTimerTask.cancel();
        return result;
    }

    /**
     * Method that shows the 3s timer while the correct answer is being displayed
     * @param score
     */
    protected void showCorrectAnswerTimer(long score) {
        enableJokers(false);
        answerTimer = new Timer();
        answerTimerTask = new TimerTask() {
            double progressTime = 3;
            int timer = 100;

            @Override
            public void run() {
                Platform.runLater(() -> timerBar.setProgress(progressTime / 10));
                if (timer == 100) {
                    changeTimerTime(progressTime);
                    timer = 0;
                }
                progressTime = progressTime - 0.01;
                timer++;
                if (progressTime < 0) {
                    this.cancel();
                    Platform.runLater(() -> goToNextScene(score));
                }
            }
        };
        this.answerTimer.schedule(answerTimerTask, 0, 10);
    }

    /**
     * Method that sends the answer that the player presses to the server and acts accordingly
     * @param answer
     */
    protected abstract void processAnswer(String answer);

    /**
     * Method that transitions from the current question to the next one
     * @param score
     */
    protected abstract void goToNextScene(long score);

    /**
     * Method that submits an empty answer with time 0 to the server
     */
    public abstract void timeOut();

    /**
     * Changes the color of the times according to textTime
     *
     * @param textTime
     */
    public void changeColor(int textTime) {
        switch (textTime) {
            case 10, 9, 8 -> timerBar.setStyle("-fx-accent: #4D8C57");
            case 7, 6 -> timerBar.setStyle("-fx-accent: #A3B56B");
            case 5, 4 -> timerBar.setStyle("-fx-accent: #FD9A01");
            case 3 -> timerBar.setStyle("-fx-accent: #FD6104");
            case 2 -> timerBar.setStyle("-fx-accent: #FF2C05");
            case 1 -> timerBar.setStyle("-fx-accent: #F00505");
            default -> timerBar.setStyle("-fx-accent: #4D8C57");
        }
    }

    /**
     * Method that resets all the UI elements to their base state
     */
    protected void resetUI() {
        scoreText.setText(null);
        timerText.setText(null);
        timerBar.setProgress(10);
        questionText.setText(null);
        enableJokers(true);
    }

    protected abstract void setUpJokers();

    /**
     * Event handler for pressing a joker button
     * @param event
     * @return jokerType pressed
     */
    @FXML
    protected JokerType jokerPress(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonId = button.getId();
        JokerData jokerData = jokerMap.get(buttonId);
        if (jokerData.isUsed()) {
            return null;
        }
        button.setDisable(true);

        myMainCtrl.useJokerSingleplayer(jokerData.getType());
        removeJokerFromList(jokerData.getType());
        return jokerData.getType();
    }

    void removeJokerFromList(JokerType type) {
        myMainCtrl.getJokerList().forEach((joker) -> {
            if (type.equals(joker.getType())) {
                joker.setUsed(true);
            }
        });
    }

    void enableJokers(boolean b) {
        jokerList.forEach((j) -> j.setDisable(!b));
    }
}


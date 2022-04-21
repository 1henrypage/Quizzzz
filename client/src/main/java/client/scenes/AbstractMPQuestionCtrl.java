package client.scenes;

import client.utils.ServerUtils;
import commons.JokerType;
import commons.Submission;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractMPQuestionCtrl extends AbstractQuestionCtrl{

    protected Submission submission;

    @FXML
    private Button rButton0;

    @FXML
    private Button rButton1;

    @FXML
    private Button rButton2;

    @FXML
    private Button rButton3;

    @FXML
    private Button rButton4;

    @FXML
    private Button rButton5;

    @FXML
    private Button rButton6;

    @FXML
    private Button rButton7;

    @FXML
    private Button rButton8;

    @FXML
    private Button rButton9;

    @FXML
    private Button rButton10;

    @FXML
    private Button rButton11;

    @FXML
    private ListView<String> informationBox;

    @FXML
    private ListView<String> playerList;

    @FXML
    private Text roundOverText;

    private double mainProgressTime;
    private double actualProgressTime;

    /**
     * Constructor for QuestionController
     *
     * @param server   that can communicate with backend
     * @param mainCtrl
     */
    public AbstractMPQuestionCtrl(ServerUtils server, MyMainCtrl mainCtrl) {
        super(server, mainCtrl);
    }

    /**
     * Gets called every time a question screen is shown
     * @param score
     * @param list
     * @param infoList
     */
    public void init(Long score, ObservableList<String> list, ObservableList<String> infoList) {
        mainProgressTime = 10;
        actualProgressTime = mainProgressTime;
        super.init(score);
        playerList.setItems(list);
        informationBox.setItems(infoList);

    }

    /**
     * Event handler for a reaction button press
     * @param event
     */
    @FXML
    protected void pressReaction(ActionEvent event) {
        Button b = (Button) event.getSource();
        String emoji = b.getText();
        myMainCtrl.sendTextToInfoBox(emoji);
    }

    /**
     * Method that displays a list of messages in the information box
     * @param list
     */
    public void displayReactions(List<String> list) {
        LinkedList<String> l = new LinkedList<>();
        for (String player : list) {
            l.addFirst(player);
        }
        informationBox.setItems(FXCollections.observableList(l));
    }

    /**
     * Method that takes care of the UI timer functionality
     */
    @Override
    public void timer() {
        mainTimer = new Timer();
        mainTimerTask = new TimerTask() {
            int timer = 100;
            boolean finish = false;
            @Override
            public void run() {
                if (actualProgressTime < 0.1) {
                    if (!finish) {
                        finish = true;
                        Platform.runLater(() -> roundOverText.setVisible(mainProgressTime > 0.5));
                        disableControls();
                    }
                    actualProgressTime = 0.1;
                }
                Platform.runLater(() -> timerBar.setProgress(actualProgressTime / 10));
                if (timer == 100) {
                    changeTimerTime();
                    timer = 0;
                }
                actualProgressTime = actualProgressTime - 0.01;
                mainProgressTime = mainProgressTime - 0.01;
                timer++;
                if (mainProgressTime < 0) {
                    endOfRound();
                }
            }
        };
        this.mainTimer.schedule(mainTimerTask, 0, 10);
    }

    protected void changeTimerTime() {
        int textTime = (int) Math.round(actualProgressTime);
        Platform.runLater(() -> timerText.setText(textTime + " s"));
        Platform.runLater(() -> changeColor(textTime));
    }

    private void endOfRound() {
        Platform.runLater(() -> timerText.setText(0 + " s"));
        mainTimerTask.cancel();
        Platform.runLater(this::timeOut);
    }

    /**
     * Method that shows the 3s timer while the correct answer is being displayed
     * @param score
     */
    protected void showCorrectAnswerTimer(long score) {
        roundOverText.setVisible(false);
        answerTimer = new Timer();
        answerTimerTask = new TimerTask() {
            double progressTime = 5;
            int timer = 100;
            boolean scoresShown = false;
            @Override
            public void run() {
                Platform.runLater(() -> timerBar.setProgress(progressTime / 10));
                if (timer == 100) {
                    int textTime = (int) progressTime;
                    Platform.runLater(() -> timerText.setText(textTime + " s"));
                    Platform.runLater(() -> changeColor(textTime));
                    timer = 0;
                }
                progressTime = progressTime - 0.01;
                timer++;
                if (progressTime > 4 && progressTime < 4.5 && !scoresShown) {
                    scoresShown = true;
                    Platform.runLater(() -> updatePlayerList());
                } else if (progressTime < 0) {
                    this.cancel();
                    check(score);
                }
            }
        };
        this.answerTimer.schedule(answerTimerTask, 0, 10);
    }

    protected void disableControls() {
        enableJokers(false);
    }

    protected abstract void goToNextScene(long score, ObservableList<String> items, ObservableList<String> items1);

    protected abstract void goToLeaderboard(long score, ObservableList<String> items, ObservableList<String> items1);

    /**
     * checkstyle
     * @param score
     */
    public void check(long score){
        if(myMainCtrl.questionCounter == 10){
            Platform.runLater(() -> goToLeaderboard(score, playerList.getItems(),
                    informationBox.getItems()));
        } else{
            Platform.runLater(() -> goToNextScene(score, playerList.getItems(), informationBox.getItems()));
        }
    }


    /**
     * Method that sends the answer that the player presses to the server and acts accordingly
     * @param answer
     */
    protected void processAnswer(String answer) {
        submission = new Submission(answer, timerBar.getProgress());
        roundOverText.setVisible(true);
    }

    /**
     * Gets called when timer runs out
     */
    public void timeOut() {
        long score = 0;
        if (submission != null) {
            score = myMainCtrl.sendMPSubmission(submission);
            submission = null;
        } else {
            score = myMainCtrl.sendMPSubmission(new Submission("late", -1));
        }
        this.scoreText.setText("Score: " + score);
        showCorrectAnswerTimer(score);
    }

    protected void updatePlayerList() {
        var list = myMainCtrl.getPlayerScores();
        playerList.setItems(FXCollections.observableList(list));
    }

    protected abstract void goToNextScene(long score, ObservableList<String> list);

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

        myMainCtrl.useJokerMultiplayer(jokerData.getType());
        removeJokerFromList(jokerData.getType());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String text = button.getText();
                switch (text) {
                    case "x2" -> myMainCtrl.sendTextToInfoBox("Used x2 joker!");
                    case "Remove" -> myMainCtrl.sendTextToInfoBox("Used Remove joker!");
                    case "Reduce Time" -> myMainCtrl.sendTextToInfoBox("Used Reduce Time joker!");
                }
            }
        }, 100);

        return jokerData.getType();
    }

    /**
     * Method used to reduce the time for the player by 30%
     */
    public void reduceTimeJokerPlayer() {
        System.out.println("reducing time");
        actualProgressTime = actualProgressTime * 0.7;
    }
}


package client.scenes;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javax.inject.Inject;

import java.util.*;

public class  MPleaderboardCtrl extends AbstractCtrl{



    @FXML
    private ListView<String> leaderboardList;

    @FXML
    private Button LobbyButton;

    @FXML
    private Label bottomText;

    private final MyMainCtrl myMainCtrl;

    private NameScreenCtrl nameScreenCtrl;


    /**
     * constructor
     * @param myMainCtrl
     * @param nameScreenCtrl 
     */
    @Inject
    public MPleaderboardCtrl(MyMainCtrl myMainCtrl, NameScreenCtrl nameScreenCtrl){
        this.myMainCtrl = myMainCtrl;
        this.nameScreenCtrl = nameScreenCtrl;
    }


    /**
     * init function endhalftime
     * @param score
     * @param items
     * @param items1
     */
    public void init_end(long score, ObservableList<String> items, ObservableList<String> items1){

        boolean placeFound = false;

        for(int i=0; i<items.size(); i++){

            String[] stringParts = items.get(i).split(": ");
            int otherScore = Integer.parseInt(stringParts[1]);

            if(score >= otherScore){
                items.add(i, myMainCtrl.playerData.getPlayerName() + ": " + score);
                placeFound = true;
                break;
            }
        }

        if(!placeFound){
            items.add(myMainCtrl.playerData.getPlayerName() + ": " + score);
        }

        leaderboardList.setItems(items);
    }

    /**
     * function for lobby button
     */
    public void lobbyButton(){
        boolean canStart = myMainCtrl.goIntoLobby(myMainCtrl.playerData.getPlayerName());
        if(!canStart){
            myMainCtrl.showMPNameScreen();
            nameScreenCtrl.mpWarningMessage.setVisible(true);
        }else {
            nameScreenCtrl.mpWarningMessage.setVisible(false);
        }
    }


    /**
     * function for back button
     */
    public void backButton(){
        myMainCtrl.showMainScreen();
    }

    /**
     * initialize function
     * @param score
     * @param items
     * @param items1
     */
    public void init_halfTime(long score, ObservableList<String> items, ObservableList<String> items1){

        boolean placeFound = false;
        int place = items.size();

        for(int i=0; i<items.size(); i++){

            String[] stringParts = items.get(i).split(": ");
            int otherScore = Integer.parseInt(stringParts[1]);

            if(score >= otherScore){
                items.add(i, myMainCtrl.playerData.getPlayerName() + ": " + score);
                placeFound = true;
                place = i;
                break;
            }
        }

        if(!placeFound){
            items.add(myMainCtrl.playerData.getPlayerName() + ": " + score);
        }

        leaderboardList.setItems(items);
        showCorrectAnswerTimer(place, score, items, items1);
    }


    /**
     * Timer for how long the screen is shown
     * @param place of players own name and score
     * @param score
     * @param items
     * @param items1
     */
    protected void showCorrectAnswerTimer(int place, long score,
                                          ObservableList<String> items, ObservableList<String> items1) {
        Timer answerTimer = new Timer();
        TimerTask answerTimerTask = new TimerTask() {
            int progressTime = 5;

            @Override
            public void run() {
                Platform.runLater(() -> bottomText.setText("Game continues in "+
                        progressTime-- +" seconds"));

                if (progressTime <= 0) {
                    this.cancel();
                    Platform.runLater(() -> items.remove(place)); //removes the player own name and score
                    Platform.runLater(() -> myMainCtrl.setNextMPQuestion(score, items, items1));
                }
            }
        };
        answerTimer.schedule(answerTimerTask, 0, 1000);
    }

}
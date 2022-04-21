package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.BadRequestException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.*;

public class MyMainCtrl extends AbstractCtrl {

    public Stage primaryStage;
    public String gameID;
    public PlayerData playerData;
    public boolean connected;
    public int questionCounter = 1;

    private ServerUtils server;

    private HashMap<String, SceneCtrlPair> screenMap;
    private ArrayList<JokerData> jokerList;

    private AbstractMPQuestionCtrl currentCtrl;

    private String css;


    /**
     * Constructor for MyMainCtrl
     */
    public MyMainCtrl(){}

    /**
     * This method stores all the scenes and opens the main screen
     * @param primaryStage
     * @param server
     * @param mainScreen
     * @param mpNameScreen
     * @param spNameScreen
     * @param lobbyScreen
     * @param spEQScreen
     * @param spMCQScreen
     * @param adminScreen
     * @param adminAddScreen
     * @param mpEQScreen
     * @param mpMCQScreen
     * @param spSelectiveScreen
     * @param mpSelectiveScreen
     * @param leaderboardScreen
     * @param quitScreen
     * @param MPhalfTimeLeaderboardScreen
     * @param MPendLeaderboard
     */
    public void init(Stage primaryStage,
                           ServerUtils server,
                           Pair<MainScreenCtrl, Parent> mainScreen,
                           Pair<NameScreenCtrl, Parent> mpNameScreen,
                           Pair<NameScreenCtrl, Parent> spNameScreen,
                           Pair<LobbyScreenCtrl, Parent> lobbyScreen,
                           Pair<SPEstimateQuestionCtrl, Parent> spEQScreen,
                           Pair<SPMultipleChoiceQuestionCtrl, Parent> spMCQScreen,
                           Pair<AdminMainCtrl, Parent> adminScreen,
                           Pair<AdminAddCtrl, Parent> adminAddScreen,
                           Pair<MPEstimateQuestionCtrl, Parent> mpEQScreen,
                           Pair<MPMultipleChoiceQuestionCtrl, Parent> mpMCQScreen,
                           Pair<SPSelectiveQuestionCtrl, Parent> spSelectiveScreen,
                           Pair<MPSelectiveQuestionCtrl, Parent> mpSelectiveScreen,
                           Pair<LeaderboardCtrl, Parent> leaderboardScreen,
                           Pair<QuitScreenCtrl,Parent> quitScreen,
                           Pair<MPleaderboardCtrl, Parent> MPhalfTimeLeaderboardScreen,
                           Pair<MPleaderboardCtrl, Parent> MPendLeaderboard) {

        this.primaryStage = primaryStage;
        this.server = server;
        this.css = "";
        screenMap = new HashMap<>();
        screenMap.put("mainScreen", new SceneCtrlPair(mainScreen.getValue(), mainScreen.getKey()));
        screenMap.put("mpNameScreen", new SceneCtrlPair(mpNameScreen.getValue(), mpNameScreen.getKey()));
        screenMap.put("spNameScreen", new SceneCtrlPair(spNameScreen.getValue(), spNameScreen.getKey()));
        screenMap.put("lobbyScreen", new SceneCtrlPair(lobbyScreen.getValue(), lobbyScreen.getKey()));
        screenMap.put("spEQScreen", new SceneCtrlPair(spEQScreen.getValue(), spEQScreen.getKey()));
        screenMap.put("spMCQScreen", new SceneCtrlPair(spMCQScreen.getValue(), spMCQScreen.getKey()));
        screenMap.put("mpEQScreen", new SceneCtrlPair(mpEQScreen.getValue(), mpEQScreen.getKey()));
        screenMap.put("mpMCQScreen", new SceneCtrlPair(mpMCQScreen.getValue(), mpMCQScreen.getKey()));
        screenMap.put("adminScreen", new SceneCtrlPair(adminScreen.getValue(), adminScreen.getKey()));
        screenMap.put("adminAddScreen", new SceneCtrlPair(adminAddScreen.getValue(), adminAddScreen.getKey()));
        screenMap.put("spSelectiveScreen", new SceneCtrlPair(spSelectiveScreen.getValue(), spSelectiveScreen.getKey()));
        screenMap.put("mpSelectiveScreen", new SceneCtrlPair(mpSelectiveScreen.getValue(), mpSelectiveScreen.getKey()));
        screenMap.put("quitScreen",new SceneCtrlPair(quitScreen.getValue(),quitScreen.getKey()));
        setUpQuit();
        init_leaderboard(leaderboardScreen, MPhalfTimeLeaderboardScreen, MPendLeaderboard);
        showUI();
    }

    /**
     * init for leaderboard, the other init function was to long
     * @param leaderboardScreen
     * @param MPhalfTimeLeaderboardScreen
     * @param MPendLeaderboard
     */
    private void init_leaderboard(Pair<LeaderboardCtrl, Parent> leaderboardScreen,
                                  Pair<MPleaderboardCtrl, Parent> MPhalfTimeLeaderboardScreen,
                                  Pair<MPleaderboardCtrl, Parent> MPendLeaderboard){
        screenMap.put("leaderboardScreen", new SceneCtrlPair(leaderboardScreen.getValue(), leaderboardScreen.getKey()));
        screenMap.put("MPhalfTimeLeaderboardScreen", new SceneCtrlPair(MPhalfTimeLeaderboardScreen.getValue(),
                MPhalfTimeLeaderboardScreen.getKey()));
        screenMap.put("MPendLeaderboardScreen", new SceneCtrlPair(MPendLeaderboard.getValue(),
                MPendLeaderboard.getKey()));
    }


    private void showUI() {
        primaryStage.setScene(new Scene(screenMap.get("mainScreen").getScene()));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        showMainScreen();
        primaryStage.show();
    }

    /**
     * This method shows the main screen
     */
    public void showMainScreen() {
        setScene("mainScreen", "ScreenCommonCSS.css");
    }

    /**
     * This method shows the name screen
     */
    public void showSPNameScreen(){
        setScene("spNameScreen", "ScreenCommonCSS.css");
    }

    /**
     * This method shows the name screen
     */
    public void showMPNameScreen(){
        setScene("mpNameScreen", "ScreenCommonCSS.css");
    }

    /**
     * This is the method that shows the quit screen
     */
    public void showQuitScreen() {
        setScene("quitScreen","ScreenCommonCSS.css");
    }

    /**
     * This method shows the name screen
     * @param players
     */
    public void showLobbyScreen(List<PlayerData> players){
        setScene("lobbyScreen", "lobbyCSS.css");
        var ctrl = (LobbyScreenCtrl) screenMap.get("lobbyScreen").getCtrl();
        ctrl.init(players);
    }

    /**
     * This method shows the name screen
     */
    public void showAdminScreen(){
        setScene("adminScreen", "AdminCSS.css");
        var ctrl = (AdminMainCtrl) screenMap.get("adminScreen").getCtrl();
        ctrl.init();
    }

    /**
     * This method shows the name screen
     * @param activity to be displayed
     */
    public void showAdminAddScreen(Activity activity){
        setScene("adminAddScreen", "AdminCSS.css");
        var ctrl = (AdminAddCtrl) screenMap.get("adminAddScreen").getCtrl();
        ctrl.init(activity);
    }

    /**
     * This method starts the game by getting a question and displaying it
     * @param name
     */
    public void startSPGame(String name) {
        if (name == null || name.length() == 0) {
            return;
        }
        gameID = server.createGame(name);
        setUpJokers(gameID);
        Question q = server.getQuestion(gameID);
        showQuestionScene(q, 0L);
    }

    /**
     * This method attempts to enter a player into a lobby
     * @param name
     * @return true if player can join with that name, false otherwise
     */
    public boolean goIntoLobby(String name) {
         playerData = new PlayerData(name);
         List<PlayerData> players = null;
        try {
            players = server.connect(playerData).getPlayerDataList();
        }catch (BadRequestException e){
            return false;
        }
        showLobbyScreen(players);
        connected = true;
        return true;
    }

    /**
     * Sets the correct scenes at the beginning of a MP game
     * @param data
     */
    public void startMPGame(LobbyData data) {
        gameID = String.valueOf(data.getAssignedGameID());
        setUpJokers(gameID);
        Question q = server.getMPQuestion(gameID, playerData.getPlayerName());
        longPollingMP();
        connected = false;
        var list = FXCollections.observableList(getPlayerScores());
        showMPQuestionScene(q, 0L, list, null);
    }

    /**
     * Sets the correct scene along with its CSS to the primaryStage
     * @param score
     * @param q the question to be displayed
     * @param list
     * @param infoList
     */
    public void showMPQuestionScene(Question q, Long score,
                                    ObservableList<String> list, ObservableList<String> infoList) {

        if (q.getType() == QuestionType.ESTIMATE) {
            setScene("mpEQScreen", "QuestionCSS.css");
            var ctrl = (MPEstimateQuestionCtrl) screenMap.get("mpEQScreen").getCtrl();
            currentCtrl = ctrl;
            ctrl.init(q, score, list, infoList);
        } else if (q.getType() == QuestionType.MC){
            setScene("mpMCQScreen", "QuestionCSS.css");
            var ctrl = (MPMultipleChoiceQuestionCtrl) screenMap.get("mpMCQScreen").getCtrl();
            currentCtrl = ctrl;
            ctrl.init(q, score, list, infoList);
        } else if (q.getType() == QuestionType.SELECTIVE) {
            setScene("mpSelectiveScreen", "QuestionCSS.css");
            var ctrl = (MPSelectiveQuestionCtrl) screenMap.get("mpSelectiveScreen").getCtrl();
            currentCtrl = ctrl;
            ctrl.init(q, score, list, infoList);
        }
    }

    /**
     * Method that sends the submission to the server
     * @param answer
     * @param time
     * @return score after updating
     */
    public long sendSubmission(String answer, double time) {
        Submission s = new Submission(answer, time);
        return server.validateQuestion(s, gameID);
    }

    /**
     * Method that sends the submission to the server
     * @param s
     * @return score after updating
     */
    public long sendMPSubmission(Submission s) {
        return server.validateMPQuestion(s, gameID, playerData.getPlayerName());
    }

    /**
     * Gets the new question and sets the scene accordingly
     * @param score
     */
    public void setNextQuestion(long score) {
        try {
            Question newQuestion = server.getQuestion(gameID);

            questionCounter++;
            if(questionCounter > 20){
                questionCounter = 1;
            }

            if (newQuestion == null) {
                showLeaderboardScreen();
                return;
            }
            showQuestionScene(newQuestion, score);
        } catch (BadRequestException e) {
            System.out.println(e);
        }
    }

    /**
     * Gets the new question and sets the scene accordingly
     * @param score
     * @param list
     * @param infoList
     */
    public void setNextMPQuestion(long score, ObservableList<String> list, ObservableList<String> infoList) {
        try {
            Question newQuestion = server.getMPQuestion(gameID, playerData.getPlayerName());

            questionCounter++;
            if(questionCounter > 20){
                questionCounter = 1;
            }

            if (newQuestion == null) {
                showMPendLeaderboard(score, list, infoList);
                stopMPLP();
                return;
            }
            showMPQuestionScene(newQuestion, score, list, infoList);
        } catch (BadRequestException e) {
            System.out.println(e);
        }
    }

    /**
     * Sets the correct scene along with its CSS to the primaryStage
     * @param score
     * @param q the question to be displayed
     */
    public void showQuestionScene(Question q, Long score) {
        if (q.getType() == QuestionType.ESTIMATE) {
            setScene("spEQScreen", "QuestionCSS.css");
            var ctrl = (SPEstimateQuestionCtrl) screenMap.get("spEQScreen").getCtrl();
            ctrl.init(q, score);
        } else if (q.getType() == QuestionType.MC) {
            setScene("spMCQScreen", "QuestionCSS.css");
            var ctrl = (SPMultipleChoiceQuestionCtrl) screenMap.get("spMCQScreen").getCtrl();
            ctrl.init(q, score);
        }else if (q.getType() == QuestionType.SELECTIVE) {
            setScene("spSelectiveScreen", "QuestionCSS.css");
            var ctrl = (SPSelectiveQuestionCtrl) screenMap.get("spSelectiveScreen").getCtrl();
            ctrl.init(q, score);
        }
    }

    /**
     * Sets the scene
     * @param screen
     * @param cssFile
     */
    private void setScene(String screen, String cssFile) {
        var scene = screenMap.get(screen).getScene();
        primaryStage.getScene().setRoot(scene);
        setCSS(cssFile);
    }

    /**
     * This method sets the CSS of a scene
     * @param fileName with the .css
     */
    public void setCSS(String fileName) {
        primaryStage.getScene().getStylesheets().remove(css);
        css = Objects.requireNonNull(getClass()
                .getResource("css/" + fileName)).toExternalForm();
        primaryStage.getScene().getStylesheets().add(css);
    }

    /**
     * Getter for jokerList
     * @return jokerList
     */
    public ArrayList<JokerData> getJokerList() {
        return jokerList;
    }

    /**
     * Method that sends the joker to the server
     * @param joker the joker type to be sent
     * @return boolean allowed/forbidden
     */
    public boolean useJokerSingleplayer(JokerType joker) {
        if (joker == JokerType.REMOVE_WRONG_ANSWER) {
            return true;
        }
        return server.useJokerSingleplayer(Long.parseLong(gameID), joker);
    }

    /**
     * Method that sends the joker to the server
     * @param joker the joker type to be sent
     * @return boolean allowed/forbidden
     */
    public boolean useJokerMultiplayer(JokerType joker) {
        if (joker == JokerType.REMOVE_WRONG_ANSWER) {
            return true;
        }
        return server.useJokerMultiplayer(Long.parseLong(gameID), playerData.getPlayerName(), joker);
    }

    /**
     * Sets up the game's jokers
     * @param gameID
     */
    public void setUpJokers(String gameID) {
        var jokers = server.getJokers(gameID);
        jokerList = new ArrayList<>();
        for (JokerType j : jokers) {
            String text;
            switch (j) {
                case DOUBLE_POINTS:
                    text = "x2";
                    jokerList.add(new JokerData(text, JokerType.DOUBLE_POINTS, false, true, true, true, true));
                    break;
                case REMOVE_WRONG_ANSWER:
                    text = "Remove";
                    jokerList.add(new JokerData(text, JokerType.REMOVE_WRONG_ANSWER, false, true, false, true, true));
                    break;
                case REDUCE_TIME:
                    text = "Reduce Time";
                    jokerList.add(new JokerData(text, JokerType.REDUCE_TIME, false, true, true, false, true));
                    break;
            }
        }
    }

    /**
     *
     * @return primary stage
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }

    /**
     * shows leaderboard screen
     */
    public void showLeaderboardScreen(){
        List<LeaderboardEntry> listLeaderboardEntries = server.getLeaderboardEntries();

        setScene("leaderboardScreen", "LeaderboardCSS.css");
        var ctrl = (LeaderboardCtrl) screenMap.get("leaderboardScreen").getCtrl();
        ctrl.init(listLeaderboardEntries);
    }

    /**
     * shows screen of half time leaderboard
     * @param score
     * @param items
     * @param items1
     */
    public void showMPhalfTimeLeaderboardScreen(long score,ObservableList<String> items,ObservableList<String> items1){
        setScene("MPhalfTimeLeaderboardScreen", "LeaderboardCSS.css");
        var ctrl = (MPleaderboardCtrl) screenMap.get("MPhalfTimeLeaderboardScreen").getCtrl();
        ctrl.init_halfTime(score, items, items1);
    }

    /**
     * show endgame leaderboard
     * @param score
     * @param items
     * @param items1
     */
    public void showMPendLeaderboard(long score,ObservableList<String> items,ObservableList<String> items1){
        setScene("MPendLeaderboardScreen", "LeaderboardCSS.css");
        var ctrl = (MPleaderboardCtrl) screenMap.get("MPendLeaderboardScreen").getCtrl();
        ctrl.init_end(score, items, items1);
    }



    /**
     * Sends the pressed emoji to the server
     * @param text
     */
    public void sendTextToInfoBox(String text) {
        server.sendToInformationBox(gameID, playerData.getPlayerName() + ": " + text);
    }

    /**
     * Sets the IP of the server to the desired ip
     *
     * @param ip The ip that the user wants to use
     */
    public void setIP(String ip) throws ConnectException {
        server.setIP(ip);
    }

    /**
     * Gets a "leaderboard" for a multiplayer game
     *
     * @return list of players connected to their scores
     */
    public List<String> getPlayerScores() {
        Map<String,Long> map = server.getPlayerScores(gameID);
        List<String> list = new ArrayList<>();
        map.keySet().forEach(k -> {
            if (!Objects.equals(k, playerData.getPlayerName())) {
                list.add(k + ": " + map.get(k));
            }
        });
        return list;
    }

    /**
     * Gets long-polling MP updates from the server
     */
    public void longPollingMP() {
        server.longPollingMP(data -> {
            if (data.getUiMessages() != null) {
                Platform.runLater(() -> currentCtrl.displayReactions(data.getUiMessages()));
            } else if (data.getWhoInitiated() != null &&
                    !data.getWhoInitiated().getPlayerName().equals(playerData.getPlayerName())) {
                Platform.runLater(() -> currentCtrl.reduceTimeJokerPlayer());
            } else {
                System.out.println("received something unexpected");
            }
        });
    }

    /**
     * Stops the long polling
     */
    public void stopMPLP(){
        server.stopMainLP();
    }

    private void setUpQuit(){
        primaryStage.setOnCloseRequest(e -> {
            var ctrl = (LobbyScreenCtrl) screenMap.get("lobbyScreen").getCtrl();
            ctrl.stop();
            stopMPLP();
            if(connected){
                server.disconnect(playerData);
            }
        });
    }

}
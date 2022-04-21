package client.scenes;

import client.utils.ServerUtils;
import commons.PlayerData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javax.inject.Inject;
import java.util.List;

public class LobbyScreenCtrl extends AbstractCtrl{

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> playerList;

    @FXML
    private Button startButton1;

    private final MyMainCtrl myMainCtrl;
    private final ServerUtils server;

    /**
     * Constructor for the lobby screen controller
     * @param myMainCtrl
     * @param server
     */
    @Inject
    public LobbyScreenCtrl(MyMainCtrl myMainCtrl, ServerUtils server) {
        this.myMainCtrl = myMainCtrl;
        this.server = server;
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     * @param players
     */
    public void init(List<PlayerData> players) {
        playerList.getItems().clear();
        for (PlayerData p : players) {
            playerList.getItems().add(p.getPlayerName());
        }
        server.longPollingLobby(l -> {
            if (l.isInStartState()) {
                Platform.runLater(() -> myMainCtrl.startMPGame(l));
                stop();
                return;
            }
            List<PlayerData> dataList = l.getPlayerDataList();
            Platform.runLater(() -> playerList.getItems().clear());
            for (PlayerData p : dataList) {
                Platform.runLater(() -> playerList.getItems().add(p.getPlayerName()));
            }
        });
    }

    /**
     *
     */
    public void goBackToMainScreen(){
        server.disconnect(myMainCtrl.playerData);
        stop();
        myMainCtrl.connected = false;
        myMainCtrl.showMainScreen();
    }

    /**
     *Event handler for Start button click
     * @param e
     */
    @FXML
    void onStartClick(ActionEvent e) {
        server.startLobby();
    }

    /**
     * Stops the long polling
     */
    public void stop(){
        server.stopLobbyLP();
    }


}

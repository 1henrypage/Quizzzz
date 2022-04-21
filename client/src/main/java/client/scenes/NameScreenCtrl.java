package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class NameScreenCtrl extends AbstractCtrl implements Initializable {

    @FXML
    public Text mpWarningMessage;

    private final MyMainCtrl myMainCtrl;

    @FXML
    private TextField SPnameField;

    @FXML
    private TextField MPnameField;


    /**
     * Constructor for the name screen controller
     * @param myMainCtrl
     */
    @Inject
    public NameScreenCtrl(MyMainCtrl myMainCtrl){
        this.myMainCtrl = myMainCtrl;
    }

    /**
     * Shows main screen
     */
    public void back() {
        myMainCtrl.showMainScreen();
    }

    /**
     * Attempts to start a singleplayer game
     */
    public void startSPGame(){
        myMainCtrl.startSPGame(SPnameField.getText());
    }

    /**
     * Attempts to start a multiplayer game
     */
    public void startMPGame() {
        boolean canStart = myMainCtrl.goIntoLobby(MPnameField.getText());
        if(canStart){
            mpWarningMessage.setVisible(false);
        }else{
            mpWarningMessage.setVisible(true);
        }

    }

    /**
     * Event handler for typing in the textField
     *
     * @param event
     */
    @FXML
    void checkForEnterSP(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            startSPGame();
        }
        return;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mpWarningMessage.setVisible(false);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void checkForBackspace(KeyEvent event){
        if(event.getCode().toString().equals("BACK_SPACE")){
            mpWarningMessage.setVisible(false);
        }else if(event.getCode().toString().equals("ENTER")){
            startMPGame();
        }
    }

}

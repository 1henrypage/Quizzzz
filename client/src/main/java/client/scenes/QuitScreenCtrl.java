package client.scenes;



import javax.inject.Inject;

public class QuitScreenCtrl extends AbstractCtrl {

    private final MyMainCtrl myMainCtrl;


    /**
     * Constructor for the quit screen controller
     *
     * @param myMainCtrl Abstract class which handles most of the controllers
     */
    @Inject
    public QuitScreenCtrl(MyMainCtrl myMainCtrl) {
        this.myMainCtrl = myMainCtrl;
    }

    /**
     * The event corresponding to the click of the button.
     * Goes back to the main screen.
     *
     */
    public void goBackToMain() {
        myMainCtrl.showMainScreen();
    }

    /**
     * The event corresponding to what happens when you quit the game
     */
    public void quitGame() {
        System.exit(0);
    }


}

package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MainModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     *
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch(args);
    }

    /**
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var mainScreen = FXML.load(MainScreenCtrl.class, "client", "scenes", "MainScreen.fxml");
        var MPnameScreen = FXML.load(NameScreenCtrl.class, "client", "scenes", "MPNameScreen.fxml");
        var SPnameScreen = FXML.load(NameScreenCtrl.class, "client", "scenes", "SPNameScreen.fxml");
        var lobbyScreen = FXML.load(LobbyScreenCtrl.class, "client", "scenes", "LobbyScreen.fxml");
        var spEQScreen = FXML.load(SPEstimateQuestionCtrl.class,
                "client", "scenes", "SPEstimateQuestion.fxml");
        var spMCQScreen = FXML.load(SPMultipleChoiceQuestionCtrl.class,
                "client", "scenes", "SPMultipleChoice.fxml");
        var mpEQScreen = FXML.load(MPEstimateQuestionCtrl.class,
                "client", "scenes", "MPEstimateQuestion.fxml");
        var mpMCQScreen = FXML.load(MPMultipleChoiceQuestionCtrl.class,
                "client", "scenes", "MPMultipleChoice.fxml");
        var mainCtrl = INJECTOR.getInstance(MyMainCtrl.class);
        var leaderboard = FXML.load(LeaderboardCtrl.class, "client", "scenes", "endLeaderboard.fxml");
        var MPhalfTimeLeaderboard = FXML.load(MPleaderboardCtrl.class, "client", "scenes", "halfTimeLeaderboard.fxml");
        var MPendLeaderboard = FXML.load(MPleaderboardCtrl.class, "client", "scenes", "MPendLeaderboard.fxml");
        var adminAddScreen = FXML.load(AdminAddCtrl.class, "client", "scenes", "AdminActivityScreen.fxml");
        var adminScreen = FXML.load(AdminMainCtrl.class, "client", "scenes", "AdminScreen.fxml");
        var spSelectiveScreen = FXML.load(SPSelectiveQuestionCtrl.class,
                "client", "scenes", "SPSelectiveQuestion.fxml");
        var quitScreen = FXML.load(QuitScreenCtrl.class,"client","scenes","QuitScreen.fxml");
        var server = INJECTOR.getInstance(ServerUtils.class);
        var mpSelectiveScreen = FXML.load(MPSelectiveQuestionCtrl.class,
                "client", "scenes", "MPSelectiveQuestion.fxml");
        mainCtrl.init(primaryStage, server, mainScreen, MPnameScreen, SPnameScreen, lobbyScreen,
                spEQScreen, spMCQScreen, adminScreen,
                adminAddScreen, mpEQScreen, mpMCQScreen, spSelectiveScreen, mpSelectiveScreen, leaderboard,
                quitScreen, MPhalfTimeLeaderboard, MPendLeaderboard);
    }
}
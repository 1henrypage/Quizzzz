package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AdminMainCtrl extends AbstractCtrl{

    @FXML
    private ListView<String> activitiesListView;

    @FXML
    private Button addButton;

    @FXML
    private TextField searchField;

    private MyMainCtrl myMainCtrl;
    private ServerUtils server;

    private List<Activity> activities;

    /**
     * Constructor for AdminMainCtrl
     *
     * @param myMainCtrl
     * @param server
     */
    @Inject
    public AdminMainCtrl(MyMainCtrl myMainCtrl, ServerUtils server) {
        this.myMainCtrl = myMainCtrl;
        this.server = server;
    }

    /**
     * Method that gets called whenever this screen is displayed
     */
    public void init() {
        this.activities = server.getActivities();
        var titles = activities.stream().map(Activity::getTitle).toList();
        updateVisibleActivities();
//        activitiesListView.setItems(FXCollections.observableList(titles));
    }

    /**
     * Event handler for Add button click
     *
     * @param event
     */
    @FXML
    void onAddClick(ActionEvent event) {
        myMainCtrl.showAdminAddScreen(new Activity(null, null, null, 0, null));
    }

    /**
     * Event handler for Back button click
     *
     * @param event
     */
    @FXML
    void onBackClick(ActionEvent event) {
        myMainCtrl.showMainScreen();
    }

    /**
     * Event handler for Back button click
     *
     * @param event
     */
    @FXML
    void onClick(MouseEvent event) {
        try {
            var x = (Text) event.getTarget();
        } catch (ClassCastException e) {
            System.out.println("Didn't click on an activity");
            return;
        }
        String value = ((Text) event.getTarget()).getText();
        var activity = activities.stream().filter(a -> a.getTitle().equals(value)).toList().get(0);
        myMainCtrl.showAdminAddScreen(activity);
    }

    /**
     * Updates Visible Activities whenever there is a key-press
     *
     * @param event The event on the keyboard that calls this method
     */
    @FXML
    void updateVisibleActivities(KeyEvent event) {
        var currentActivities = server.getActivities();
        var currentTitles = currentActivities.stream().map(Activity::getTitle).toList();
        if (searchField.getText().length() == 0) {
            activitiesListView.setItems(FXCollections.observableList(currentTitles));
        } else {
            var newList = currentTitles
                    .stream()
                    .filter(x -> StringUtils.containsIgnoreCase(x,searchField.getText()))
                    .toList();
            activitiesListView.setItems(FXCollections.observableList(newList));
        }
    }

    /**
     * Method that gets called whenever the screen is initialised
     */
    void updateVisibleActivities() {
        var currentActivities = server.getActivities();
        var currentTitles = currentActivities.stream().map(Activity::getTitle).toList();
        if (searchField.getText().length() == 0) {
            activitiesListView.setItems(FXCollections.observableList(currentTitles));
        } else {
            var newList = currentTitles
                    .stream()
                    .filter(x -> StringUtils.containsIgnoreCase(x,searchField.getText()))
                    .toList();
            activitiesListView.setItems(FXCollections.observableList(newList));
        }
    }



}


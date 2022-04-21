package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import jakarta.ws.rs.BadRequestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;

public class AdminAddCtrl extends AbstractCtrl{

    @FXML
    private TextField idTextField;

    @FXML
    private TextField consumptionTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField sourceTextField;

    @FXML
    private TextField titleTextField;

    private MyMainCtrl myMainCtrl;
    private ServerUtils server;
    private Activity activity;
    private byte[] image;

    /**
     * Constructor for AdminAddCtrl
     * @param myMainCtrl
     * @param server
     */
    @Inject
    public AdminAddCtrl(MyMainCtrl myMainCtrl, ServerUtils server) {
        this.myMainCtrl = myMainCtrl;
        this.server = server;
    }

    /**
     * Gets called every time the scene is displayed
     * @param activity
     */
    public void init(Activity activity) {
        this.activity = activity;
        image = null;
        idTextField.setText(activity.getId());
        consumptionTextField.setText(String.valueOf(activity.getEnergyConsumption()));
        sourceTextField.setText(activity.getSource());
        titleTextField.setText(activity.getTitle());
        deleteButton.setDisable(activity.getId() == null);
    }

    /**
     * Event handler for Back button click
     * @param ev
     */
    @FXML
    void onBackClick(ActionEvent ev) {
        myMainCtrl.showAdminScreen();
    }

    /**
     * Event handler for Save button click
     * @param ev
     */
    @FXML
    void onSaveClick(ActionEvent ev) {
        double c = -1;
        try {
            c = Double.parseDouble(consumptionTextField.getText());
        } catch (NumberFormatException e){
            System.out.println(e);
            return;
        }
        String id = idTextField.getText();
        String title = titleTextField.getText();
        String source = sourceTextField.getText();
        String imagePath = this.activity.getImagePath();
        Activity activity = new Activity(id, imagePath, title, c, source);
        try{
            String oldId = this.activity.getId();
            if (!(oldId == null) && !id.equals(oldId)) {
                server.deleteActivity(oldId);
            }
            server.saveActivity(activity);
            if (image != null) {
                server.uploadImage(imagePath, image);
            }
        } catch (BadRequestException e) {
            System.out.println("There was a problem with saving");
        }
        onBackClick(null);
    }

    /**
     * Event handler for Delete button click
     * @param ev
     */
    @FXML
    void onDeleteClick(ActionEvent ev) {
        try{
            server.deleteActivity(activity.getId());
            onBackClick(null);
        } catch (BadRequestException e) {
            System.out.println("There was a problem with deleting");
        }
    }

    /**
     * Event handler for Back button click
     * @param ev
     */
    @FXML
    void onUploadClick(ActionEvent ev) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(myMainCtrl.getPrimaryStage());
        String selectedFilePath = selectedFile.getPath();
        String newExtension = selectedFilePath.substring(selectedFilePath.lastIndexOf("."));
        if (activity.getImagePath() == null) {
            activity.setImagePath(idTextField.getText() + newExtension);
        } else {
            String oldFilePath = activity.getImagePath();
            String oldExtension = oldFilePath.substring(oldFilePath.lastIndexOf(".") + 1);
            activity.setImagePath(oldFilePath.replace(oldExtension, newExtension));
        }
        try {
            image = Files.readAllBytes(selectedFile.toPath());
        } catch (Exception e) {
            System.out.println("File couldn't be uploaded");
        }

    }

}

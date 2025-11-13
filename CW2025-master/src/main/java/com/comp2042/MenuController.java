package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MenuController {

    @FXML private Button playButton;
    @FXML private Button highScoreButton;
    @FXML private ToggleButton musicToggle;
    @FXML private Button exitButton;

    // PLAY button → switch from Menu.fxml to Gui.fxml (your game)
    @FXML
    private void onPlayClicked(ActionEvent event) throws Exception {
        //try to load file safely from resources
        var location = getClass().getClassLoader().getResource("gamelayout.fxml");
        if(location == null){
            throw new IllegalStateException("fxml not found");
        }
        //load fxml file
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = (Parent) loader.load(); //build UI tree

        // get javaFx for the game screen
        GuiController guiController = loader.getController();

        // game logic starting
        new GameController(guiController);

        Stage stage = (Stage) playButton.getScene().getWindow();
        Scene gameScene = new Scene(root, 300, 510);
        stage.setScene(gameScene);
        stage.setTitle("TetrisJFX - Game");
        stage.show();
    }

    // HIGH SCORE button → placeholder (you can implement later)
    @FXML
    private void onHighScoreClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Feature not implemented yet!");
        alert.setContentText("You can implement a scoreboard here later.");
        alert.showAndWait();
    }

    //Music Controller
    @FXML
    private void onMusicToggle(ActionEvent event) {
        boolean isOn = musicToggle.isSelected();

        if (isOn) {
            // Turn music ON
            AudioManager.playBackground();
            musicToggle.setText("Music: ON");
        } else {
            // Turn music OFF
            AudioManager.pauseBackground();
            musicToggle.setText("Music: OFF");
        }
    }


    // EXIT button → close the app
    @FXML
    private void onExitClicked(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}

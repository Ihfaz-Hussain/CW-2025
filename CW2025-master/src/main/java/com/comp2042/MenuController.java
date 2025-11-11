package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Gui.fxml"));
        Scene gameScene = new Scene(loader.load());
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setScene(gameScene);
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

    // On off toggle for music
    @FXML
    private void onMusicToggle(ActionEvent event) {
        boolean isOn = musicToggle.isSelected();
        musicToggle.setText(isOn ? "Music: ON" : "Music: OFF");
        System.out.println("Music toggled: " + (isOn ? "ON" : "OFF"));
    }

    // EXIT button → close the app
    @FXML
    private void onExitClicked(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}

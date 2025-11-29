package com.comp2042.controller;

import com.comp2042.view.GuiController;
import com.comp2042.model.HighScoreManager;
import com.comp2042.audio.AudioManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MenuController {

    @FXML
    private Button playButton;
    @FXML
    private Button highScoreButton;
    @FXML
    private ToggleButton musicToggle;
    @FXML
    private Button exitButton;

    // PLAY button → show player name dialog then start game
    @FXML
    private void onPlayClicked(ActionEvent event) throws Exception {
        String playerName = showPlayerNameDialog();
        
        if (playerName != null) {
            startGame(playerName);
        }
    }
    
    private String showPlayerNameDialog() {
        try {
            // Load the player name dialog
            var location = getClass().getClassLoader().getResource("PlayerNameDialog.fxml");
            if (location == null) {
                throw new IllegalStateException("PlayerNameDialog.fxml not found");
            }
            
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            PlayerNameController controller = loader.getController();
            
            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("TetrisJFX - Player Name");
            dialogStage.setScene(new Scene(root, 400, 300));
            dialogStage.setResizable(false);
            
            // Copy stylesheets from current scene
            dialogStage.getScene().getStylesheets().addAll(playButton.getScene().getStylesheets());
            
            // Show dialog and wait for it to close
            dialogStage.showAndWait();
            
            // Return player name if game was started
            if (controller.isGameStarted()) {
                return controller.getPlayerName();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void startGame(String playerName) {
        try {
            // try to load file safely from resources
            var location = getClass().getClassLoader().getResource("gameLayout.fxml");
            if (location == null) {
                throw new IllegalStateException("fxml not found");
            }
            // load fxml file
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = (Parent) loader.load(); // build UI tree

            // get javaFx for the game screen
            GuiController guiController = loader.getController();

            // game logic starting with player name
            new GameController(guiController, playerName);

            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene gameScene = new Scene(root, 800, 800);
            stage.setScene(gameScene);
            stage.setTitle("TetrisJFX - Game");

            // Center the window on screen
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // HIGH SCORE button
    @FXML
    private void onHighScoreClicked(ActionEvent event) {
        try {
            // Load the high score view FXML
            var location = getClass().getClassLoader().getResource("HighScoreView.fxml");
            if (location == null) {
                throw new IllegalStateException("HighScoreView.fxml not found");
            }
            
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            
            // Get the current stage and switch to high score scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene highScoreScene = new Scene(root, 800, 800);
            
            // Copy stylesheets from current scene
            highScoreScene.getStylesheets().addAll(((Button) event.getSource()).getScene().getStylesheets());
            
            stage.setScene(highScoreScene);
            stage.setTitle("TetrisJFX - High Scores");
            
            // Center the window on screen
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Music Controller
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

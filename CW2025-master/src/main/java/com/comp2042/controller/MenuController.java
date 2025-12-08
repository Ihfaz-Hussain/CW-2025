package com.comp2042.controller;

import com.comp2042.view.GuiController;
import com.comp2042.audio.AudioManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the main menu screen.
 * Handles navigation to game, high scores, music toggle, and exit.
 */
public class MenuController {

    @FXML
    private Button playButton;
    @FXML
    private Button highScoreButton;
    @FXML
    private ToggleButton musicToggle;
    @FXML
    private Button exitButton;

    /**
     * Handles the play button click event.
     * Shows the player name dialog and starts the game if a name is entered.
     *
     * @param event the action event from the button click
     * @throws Exception if FXML loading fails
     */
    @FXML
    private void onPlayClicked(ActionEvent event) throws Exception {
        String playerName = showPlayerNameDialog();

        if (playerName != null) {
            startGame(playerName);
        }
    }

    /**
     * Displays the player name input dialog.
     *
     * @return the entered player name, or {@code null} if cancelled
     */
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

    /**
     * Starts a new game with the specified player name.
     * Loads the game layout and initializes the game controller.
     *
     * @param playerName the name of the player
     */
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

    /**
     * Handles the high score button click event.
     * Navigates to the high score view screen.
     *
     * @param event the action event from the button click
     */
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

    /**
     * Handles the music toggle button event.
     * Turns background music on or off based on toggle state.
     *
     * @param event the action event from the toggle button
     */
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

    /**
     * Handles the exit button click event.
     * Closes the application window.
     *
     * @param event the action event from the button click
     */
    @FXML
    private void onExitClicked(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}

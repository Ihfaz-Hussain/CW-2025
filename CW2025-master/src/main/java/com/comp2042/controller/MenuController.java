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
import java.util.List;

public class MenuController {

    @FXML private Button playButton;
    @FXML private Button highScoreButton;
    @FXML private ToggleButton musicToggle;
    @FXML private Button exitButton;

    // PLAY button → switch from Menu.fxml to Gui.fxml (your game)
    @FXML
    private void onPlayClicked(ActionEvent event) throws Exception {
        //try to load file safely from resources
        var location = getClass().getClassLoader().getResource("gameLayout.fxml");
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
        Scene gameScene = new Scene(root, 720, 660);
        stage.setScene(gameScene);
        stage.setTitle("TetrisJFX - Game");
        stage.show();
    }

    // HIGH SCORE button
    @FXML
    private void onHighScoreClicked(ActionEvent event) {
        // 1. Load the current top 3 scores from the file
        List<Integer> scores = HighScoreManager.loadHighScores();

        // 2. Create a new window (Stage) that will show the leaderboard
        Stage dialog = new Stage();
        dialog.setTitle("High Scores");

        // Make this window modal: user must close it before going back to menu
        dialog.initModality(Modality.APPLICATION_MODAL);

        // 3. Create a vertical layout (VBox) for title, list, and button
        VBox root = new VBox(15);  // 15 px spacing between children
        root.setAlignment(Pos.CENTER); // Center everything
        root.setPadding(new Insets(20)); // Add padding inside the VBox

        //adding class to do css styling
        root.getStyleClass().add("highscore-root");

        //Create and style the title label
        Label title = new Label("Top 3 High Scores");
        title.getStyleClass().add("highscore-title"); // CSS class
        root.getChildren().add(title);

        //Highscore entries (1st, 2nd, 3rd place)
        if (scores.isEmpty()) {
            // If there are no scores yet, show a friendly message
            Label emptyLabel = new Label("No scores yet.\nGo play a game first!");
            emptyLabel.setWrapText(true); // Allow multi-line text
            emptyLabel.getStyleClass().add("highscore-empty");
            root.getChildren().add(emptyLabel);
        } else {
            // We always show 3 lines: if fewer scores, fill with ---
            for (int i = 0; i < 3; i++) {
                String text;
                if (i < scores.size()) {
                    text = String.format("%d) %d", i + 1, scores.get(i));
                } else {
                    text = String.format("%d) ---", i + 1);
                }

                Label entry = new Label(text);
                entry.getStyleClass().add("highscore-entry"); // gamer-style label
                root.getChildren().add(entry);
            }
        }

        // 6. Create the Back button to return to main menu
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("highscore-back-button");

        // When clicked, simply close the dialog window
        backButton.setOnAction(e -> dialog.close());

        root.getChildren().add(backButton);

        // 7. Create a Scene for this window and attach our CSS
        Scene scene = new Scene(root, 280, 260);

        Scene parentScene = ((Button) event.getSource()).getScene();
        scene.getStylesheets().addAll(parentScene.getStylesheets());

        dialog.setScene(scene);

        // 8. Show the leaderboard window and wait until the user closes it
        dialog.showAndWait();
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

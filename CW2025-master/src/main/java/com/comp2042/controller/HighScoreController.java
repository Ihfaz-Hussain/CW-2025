package com.comp2042.controller;

import com.comp2042.model.HighScoreManager;
import com.comp2042.model.HighScoreEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HighScoreController {
    
    @FXML
    private VBox scoresContainer;
    
    @FXML
    private Button backButton;
    
    @FXML
    public void initialize() {
        loadHighScores();
        setupBackButton();
    }
    
    private void loadHighScores() {
        // Load current high scores with player names
        List<HighScoreEntry> entries = HighScoreManager.loadHighScores();
        
        // Clear existing content
        scoresContainer.getChildren().clear();
        
        if (entries.isEmpty()) {
            // If there are no scores yet, show a friendly message
            Label emptyLabel = new Label("No scores yet.\nGo play a game first!");
            emptyLabel.setWrapText(true);
            emptyLabel.getStyleClass().add("highscore-empty");
            scoresContainer.getChildren().add(emptyLabel);
        } else {
            // We always show 3 lines: if fewer scores, fill with ---
            for (int i = 0; i < 3; i++) {
                String text;
                if (i < entries.size()) {
                    HighScoreEntry entry = entries.get(i);
                    text = String.format("%d) %s - %d", i + 1, entry.getPlayerName(), entry.getScore());
                } else {
                    text = String.format("%d) ---", i + 1);
                }
                
                Label entryLabel = new Label(text);
                entryLabel.getStyleClass().add("highscore-entry");
                scoresContainer.getChildren().add(entryLabel);
            }
        }
    }
    
    private void setupBackButton() {
        backButton.setOnAction(event -> returnToMenu());
    }
    
    private void returnToMenu() {
        try {
            // Load the main menu FXML
            URL location = getClass().getClassLoader().getResource("Menu.fxml");
            if (location == null) {
                throw new IllegalStateException("Menu.fxml not found");
            }
            
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            
            // Get the current stage and switch to menu scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene menuScene = new Scene(root, 800, 800);
            
            // Copy stylesheets from current scene
            menuScene.getStylesheets().addAll(backButton.getScene().getStylesheets());
            
            stage.setScene(menuScene);
            stage.setTitle("TetrisJFX - Menu");
            
            // Center the window on screen
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

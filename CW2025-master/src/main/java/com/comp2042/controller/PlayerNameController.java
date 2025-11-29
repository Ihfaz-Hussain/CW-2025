package com.comp2042.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlayerNameController {
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private Button startButton;
    
    private String playerName;
    private boolean gameStarted = false;
    
    @FXML
    public void initialize() {
        setupStartButton();
        setupTextField();
    }
    
    private void setupStartButton() {
        startButton.setOnAction(event -> startGame());
    }
    
    private void setupTextField() {
        // Allow starting game with Enter key
        nameTextField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                startGame();
            }
        });
    }
    
    private void startGame() {
        String name = nameTextField.getText().trim();
        
        if (name.isEmpty()) {
            name = "Anonymous"; // Default name if empty
        }
        
        this.playerName = name;
        this.gameStarted = true;
        
        // Close the dialog
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }
}

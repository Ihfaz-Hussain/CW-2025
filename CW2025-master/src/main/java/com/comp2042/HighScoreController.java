package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.List;

public class HighScoreController {

    @FXML private Label score1Label;
    @FXML private Label score2Label;
    @FXML private Label score3Label;

    public void setScores(List<Integer> scores) {
        if (scores.size() > 0) score1Label.setText("1. " + scores.get(0));
        if (scores.size() > 1) score2Label.setText("2. " + scores.get(1));
        if (scores.size() > 2) score3Label.setText("3. " + scores.get(2));
    }

    @FXML
    private void onBackClicked(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            var location = getClass().getClassLoader().getResource("Menu.fxml");
            if (location == null) {
                throw new IllegalStateException("Menu.fxml not found");
            }
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(location);
            Parent menuRoot = loader.load();
            scene.setRoot(menuRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

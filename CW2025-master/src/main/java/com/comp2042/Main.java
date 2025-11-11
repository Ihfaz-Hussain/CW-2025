package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Step 1. Find and load Menu.fxml from the resources folder
        // The file must be inside src/main/resources/
        URL location = getClass().getClassLoader().getResource("Menu.fxml");
        if (location == null) {
            throw new IllegalStateException("Menu.fxml not found in resources folder");
        }

        // Step 2. Load the FXML and create its JavaFX structure (scene graph)
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        // Step 3. Set up the window (Stage)
        primaryStage.setTitle("TetrisJFX - Menu");

        // Create a Scene that holds the FXML layout
        Scene scene = new Scene(root, 300, 510);

        // Step 4. Attach the Scene to the Stage (window)
        primaryStage.setScene(scene);
        primaryStage.show();

        // At this point the app shows the MENU screen.
        // The game will only load when the Play button is clicked (handled in MenuController).
    }

    public static void main(String[] args) {
        // JavaFX apps always start here
        launch(args);
    }
}

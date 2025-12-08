package com.comp2042.view;

import com.comp2042.audio.AudioManager;
import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import com.comp2042.model.GameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;
import com.comp2042.animation.BoardShake;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller for the main game view.
 * Manages the game UI, handles keyboard input, and coordinates rendering.
 */
public class GuiController implements Initializable {

    private GameRenderer gameRenderer;

    @FXML
    private Button pauseButton;
    @FXML
    private Button musicButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button restartButton;
    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private GameOverPanel gameOverPanel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private GridPane nextPiecePanel;
    @FXML
    private GridPane holdPiecePanel;

    @FXML
    private Label linesLabel;

    @FXML
    private BorderPane gameBoard;

    private ViewData currentViewData;

    private InputEventListener eventListener;

    private GameTimer gameTimer;

    private InputHandler inputHandler;

    private final ObjectProperty<GameState> gameState = new SimpleObjectProperty<>(GameState.RUNNING);

    /**
     * Checks if the game is currently running.
     *
     * @return {@code true} if the game state is RUNNING, {@code false} otherwise
     */
    public boolean isRunning() {
        return gameState.get() == GameState.RUNNING;
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return {@code true} if the game state is PAUSED, {@code false} otherwise
     */
    public boolean isPaused() {
        return gameState.get() == GameState.PAUSED;
    }

    /**
     * Checks if the game is over.
     *
     * @return {@code true} if the game state is OVER, {@code false} otherwise
     */
    public boolean isOver() {
        return gameState.get() == GameState.OVER;
    }

    // --- Initialization ---

    /**
     * Initializes the controller after the FXML has been loaded.
     * Sets up fonts, input handlers, keyboard events, and the game renderer.
     *
     * @param location  the location used to resolve relative paths, or {@code null}
     * @param resources the resources used to localize the root object, or
     *                  {@code null}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(
                getClass().getClassLoader().getResource("digital.ttf").toExternalForm(),
                38);

        // Initialize input handler
        inputHandler = new InputHandler();

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Single, clean key handler using InputHandler
        gamePanel.setOnKeyPressed(event -> {
            if (!isRunning()) {
                return;
            }

            Runnable action = inputHandler.getActionForKey(event.getCode());
            if (action != null) {
                action.run();
                event.consume();
            }
        });

        gameOverPanel.setVisible(false);

        // Sync music button text with current audio state
        if (musicButton != null) {
            musicButton.setText(AudioManager.isPlaying() ? "Music: ON" : "Music: OFF");
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        // Initialize GameRenderer
        gameRenderer = new GameRenderer(gamePanel, brickPanel, nextPiecePanel, holdPiecePanel);
    }

    /**
     * Initializes the game view with the board matrix and initial brick data.
     * Sets up listeners for panel position changes and starts the game timer.
     *
     * @param boardMatrix the 2D array representing the game board
     * @param brick       the initial brick view data
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        this.currentViewData = brick;
        gameRenderer.initGameView(boardMatrix, brick);

        gamePanel.layoutXProperty().addListener((obs, oldVal, newVal) -> updateBrickPanelPosition(currentViewData));
        gamePanel.layoutYProperty().addListener((obs, oldVal, newVal) -> updateBrickPanelPosition(currentViewData));

        // Initialize game timer
        gameTimer = new GameTimer(eventListener,
                () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));

        // Start the falling timer
        gameTimer.start();
    }

    /**
     * Updates the position of the brick panel based on current brick data.
     * Does nothing if the game is paused.
     *
     * @param brick the current brick view data
     */
    private void updateBrickPanelPosition(ViewData brick) {
        if (isPaused()) {
            return;
        }
        gameRenderer.updateBrickPanelPosition(brick);
    }

    /**
     * Refreshes the display of the current falling brick.
     *
     * @param brick the updated brick view data
     */
    public void refreshBrick(ViewData brick) {
        this.currentViewData = brick;
        gameRenderer.refreshBrick(brick);
    }

    /**
     * Refreshes the game background with the updated board state.
     *
     * @param board the 2D array representing the current board state
     */
    public void refreshGameBackground(int[][] board) {
        gameRenderer.refreshGameBackground(board);
    }

    // --- Game Logic Delegates ---

    /**
     * Handles visual effects when lines are cleared.
     * Shows score notification and triggers board shake animation.
     *
     * @param downData the result data from the down movement
     */
    private void handleLineClearEffects(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel(
                    "+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
            BoardShake.play(gameBoard);
        }
    }

    /**
     * Moves the current brick down by one row.
     * Handles line clear effects and refreshes the display.
     *
     * @param event the move event containing event type and source
     */
    public void moveDown(MoveEvent event) {
        if (isRunning()) {
            DownData downData = eventListener.onDownEvent(event);
            handleLineClearEffects(downData);
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Instantly drops the current brick to the bottom.
     * Handles line clear effects and spawns a new brick.
     *
     * @param event the move event containing event type and source
     */
    public void hardDrop(MoveEvent event) {
        if (isRunning()) {
            DownData downData = eventListener.onHardDropEvent(event);
            handleLineClearEffects(downData);
            // After hard drop, we want to show the *new* active brick
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    // --- Input & Setup ---

    /**
     * Sets the event listener for handling game input events.
     *
     * @param eventListener the listener to handle input events
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        inputHandler.setEventListener(eventListener);
        inputHandler.setGuiController(this);
    }

    /**
     * Binds the game board to the scene for responsive scaling.
     * Adjusts scale based on window dimensions while maintaining aspect ratio.
     *
     * @param scene the JavaFX scene to bind to
     */
    public void bindToScene(Scene scene) {
        // Make game board responsive to window size using CSS scaling
        DoubleBinding scaleBinding = Bindings.createDoubleBinding(() -> {
            // Calculate scale factor based on window dimensions
            double baseWidth = 800;
            double baseHeight = 780;
            double currentWidth = scene.getWidth();
            double currentHeight = scene.getHeight();

            // Calculate scale factors
            double widthScale = currentWidth / baseWidth;
            double heightScale = currentHeight / baseHeight;

            // Use the smaller scale to maintain aspect ratio
            return Math.min(widthScale, heightScale);
        }, scene.widthProperty(), scene.heightProperty());

        // Apply scale to game board
        scaleBinding.addListener((obs, oldVal, newVal) -> {
            double scale = newVal.doubleValue();
            // Apply scale transform to game board
            gameBoard.setScaleX(scale);
            gameBoard.setScaleY(scale);
        });

        // Initial scale
        gameBoard.setScaleX(scaleBinding.get());
        gameBoard.setScaleY(scaleBinding.get());
        gameBoard.setScaleY(scaleBinding.get());
    }

    // --- UI Binding ---

    /**
     * Binds the score label to an integer property for automatic updates.
     *
     * @param integerProperty the score property to bind to
     */
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null && integerProperty != null) {
            scoreLabel.textProperty().bind(
                    Bindings.concat("Score: ", integerProperty));
        }
    }

    /**
     * Binds the level label to a level property and adjusts game speed accordingly.
     *
     * @param levelProperty the level property to bind to
     */
    public void bindLevel(IntegerProperty levelProperty) {
        if (levelLabel != null && levelProperty != null) {

            // Show "Level: X" in the label
            levelLabel.textProperty().bind(
                    Bindings.concat("Level: ", levelProperty));

            // When the level changes, adjust the falling speed
            levelProperty.addListener((obs, oldVal, newVal) -> {
                int level = newVal.intValue();
                gameTimer.updateSpeedForLevel(level);
            });

            // Make sure speed matches initial level (1) when game starts
            gameTimer.updateSpeedForLevel(levelProperty.get());
        }
    }

    /**
     * Binds the lines label to a lines property for automatic updates.
     *
     * @param linesProperty the lines cleared property to bind to
     */
    public void bindLines(IntegerProperty linesProperty) {
        if (linesLabel != null && linesProperty != null) {
            linesLabel.textProperty().bind(
                    Bindings.concat("Lines: ", linesProperty));
        }
    }

    // --- Game Lifecycle ---

    /**
     * Triggers the game over state.
     * Stops the timer, shows the game over panel, and updates state.
     */
    public void gameOver() {
        gameTimer.stop();
        gameOverPanel.setVisible(true);
        gameState.set(GameState.OVER);
        inputHandler.setGameOver(true);
    }

    /**
     * Starts a new game, resetting the board and timer.
     *
     * @param actionEvent the action event from the UI button
     */
    public void newGame(ActionEvent actionEvent) {
        gameTimer.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        gamePanel.requestFocus();
        gameTimer.start();
        gameState.set(GameState.RUNNING);
        inputHandler.setPaused(false);
        inputHandler.setGameOver(false);
    }

    /**
     * Toggles the pause state of the game.
     * Does nothing if the game is already over.
     *
     * @param actionEvent the action event from the UI button
     */
    public void pauseGame(ActionEvent actionEvent) {
        // Do nothing if the game is already over
        if (isOver()) {
            return;
        }

        // Toggle pause state
        if (isRunning()) {
            // Currently running → pause it
            gameTimer.pause();
            gameState.set(GameState.PAUSED);
            inputHandler.setPaused(true);
            if (pauseButton != null) {
                pauseButton.setText("Resume");
            }
        } else {
            // Currently paused → resume it
            gameTimer.resume();
            gameState.set(GameState.RUNNING);
            inputHandler.setPaused(false);
            if (pauseButton != null) {
                pauseButton.setText("Pause");
            }
        }

        // Keep keyboard focus on the game panel
        gamePanel.requestFocus();
    }

    /**
     * Toggles the background music on or off.
     *
     * @param actionEvent the action event from the UI button
     */
    public void toggleMusic(ActionEvent actionEvent) {
        if (AudioManager.isPlaying()) {
            AudioManager.pauseBackground();
            if (musicButton != null) {
                musicButton.setText("Music: OFF");
            }
        } else {
            AudioManager.playBackground();
            if (musicButton != null) {
                musicButton.setText("Music: ON");
            }
        }
        gamePanel.requestFocus();
    }

    // --- Navigation ---

    /**
     * Returns to the main menu screen.
     * Stops the game timer and loads the menu FXML.
     *
     * @param actionEvent the action event from the UI button
     */
    public void returnToMenu(ActionEvent actionEvent) {
        try {
            // Stop the game timer
            gameTimer.stop();

            // Load the main menu FXML
            var location = getClass().getClassLoader().getResource("Menu.fxml");
            if (location == null) {
                throw new IllegalStateException("Menu.fxml not found");
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            // Get the current stage and switch to menu scene
            Stage stage = (Stage) gamePanel.getScene().getWindow();
            Scene menuScene = new Scene(root, 800, 800);
            stage.setScene(menuScene);
            stage.setTitle("TetrisJFX - Menu");

            // Center the window on screen
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Restarts the current game from the beginning.
     * Resets the board, score, and timer.
     *
     * @param actionEvent the action event from the UI button
     */
    public void restartGame(ActionEvent actionEvent) {
        // Stop the current game
        gameTimer.stop();

        // Reset game state
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();

        // Reset pause state
        gameState.set(GameState.RUNNING);
        inputHandler.setPaused(false);
        inputHandler.setGameOver(false);
        if (pauseButton != null) {
            pauseButton.setText("Pause");
        }

        // Restart the timer
        gameTimer.start();
        gamePanel.requestFocus();
    }

    /**
     * Gets the score label component.
     *
     * @return the score label
     */
    public Label getScorelabel() {
        return scoreLabel;
    }

    /**
     * Sets the score label component.
     *
     * @param scorelabel the score label to set
     */
    public void setScorelabel(Label scorelabel) {
        this.scoreLabel = scorelabel;
    }
}

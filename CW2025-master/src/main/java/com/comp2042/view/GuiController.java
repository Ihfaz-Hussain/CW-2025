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

    public boolean isRunning() {
        return gameState.get() == GameState.RUNNING;
    }


    public boolean isPaused() {
        return gameState.get() == GameState.PAUSED;
    }

    public boolean isOver() {
        return gameState.get() == GameState.OVER;
    }

    //Initialization

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

    private void updateBrickPanelPosition(ViewData brick) {
        if (isPaused()) {
            return;
        }
        gameRenderer.updateBrickPanelPosition(brick);
    }

    public void refreshBrick(ViewData brick) {
        this.currentViewData = brick;
        gameRenderer.refreshBrick(brick);
    }

    public void refreshGameBackground(int[][] board) {
        gameRenderer.refreshGameBackground(board);
    }

    // Game Logic

    private void handleLineClearEffects(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel(
                    "+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
            BoardShake.play(gameBoard);
        }
    }

    public void moveDown(MoveEvent event) {
        if (isRunning()) {
            DownData downData = eventListener.onDownEvent(event);
            handleLineClearEffects(downData);
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void hardDrop(MoveEvent event) {
        if (isRunning()) {
            DownData downData = eventListener.onHardDropEvent(event);
            handleLineClearEffects(downData);
            // After hard drop, we want to show the *new* active brick
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    //Input & Setup

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        inputHandler.setEventListener(eventListener);
        inputHandler.setGuiController(this);
    }

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

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null && integerProperty != null) {
            scoreLabel.textProperty().bind(
                    Bindings.concat("Score: ", integerProperty));
        }
    }

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

    public void bindLines(IntegerProperty linesProperty) {
        if (linesLabel != null && linesProperty != null) {
            linesLabel.textProperty().bind(
                    Bindings.concat("Lines: ", linesProperty));
        }
    }

    // --- Game Lifecycle ---

    public void gameOver() {
        gameTimer.stop();
        gameOverPanel.setVisible(true);
        gameState.set(GameState.OVER);
        inputHandler.setGameOver(true);
    }

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

    public Label getScorelabel() {

        return scoreLabel;
    }

    public void setScorelabel(Label scorelabel) {

        this.scoreLabel = scorelabel;
    }
}

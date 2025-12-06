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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 30;
    private static final int BRICK_WIDTH = 33;
    private static final int HIDDEN_ROWS = 2; // rows at the top we don’t show

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
    private BorderPane gameBoard;

    private Rectangle[][] displayMatrix;

    private ViewData currentViewData;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] nextPieceRectangles;

    private Rectangle[][] holdPieceRectangles;

    private GameTimer gameTimer;

    private InputHandler inputHandler;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

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
            if (isPause.getValue() || isGameOver.getValue()) {
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
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        this.currentViewData = brick;
        // Create rectangle grid for the visible rows (board rows 2..end)
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        int visibleRows = boardMatrix.length - HIDDEN_ROWS;
        int columns = boardMatrix[0].length;

        // Set explicit size for gamePanel to eliminate padding/deadzone
        // Gaps only exist between cells, not after the last cell
        double prefWidth = columns * BRICK_WIDTH + (columns - 1) * gamePanel.getHgap();
        double prefHeight = visibleRows * BRICK_SIZE + (visibleRows - 1) * gamePanel.getVgap();
        gamePanel.setPrefWidth(prefWidth);
        gamePanel.setPrefHeight(prefHeight);
        gamePanel.setMinWidth(prefWidth);
        gamePanel.setMinHeight(prefHeight);
        gamePanel.setMaxWidth(prefWidth);
        gamePanel.setMaxHeight(prefHeight);

        // Also constrain the gameBoard BorderPane to match the game grid size
        gameBoard.setPrefWidth(prefWidth);
        gameBoard.setPrefHeight(prefHeight);
        gameBoard.setMaxWidth(prefWidth);
        gameBoard.setMaxHeight(prefHeight);

        for (int row = HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                Rectangle rectangle = new Rectangle(BRICK_WIDTH, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);

                displayMatrix[row][col] = rectangle;

                // visible row index = board row index - hidden rows
                int visibleRow = row - HIDDEN_ROWS;
                gamePanel.add(rectangle, col, visibleRow);
            }
        }

        // Create rectangles for the falling brick in a separate small GridPane
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        brickPanel.getChildren().clear();

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_WIDTH, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // Position the brick according to its board coordinates
        brickPanel.setManaged(false);
        updateBrickPanelPosition(brick);

        gamePanel.layoutXProperty().addListener((obs, oldVal, newVal) -> updateBrickPanelPosition(currentViewData));
        gamePanel.layoutYProperty().addListener((obs, oldVal, newVal) -> updateBrickPanelPosition(currentViewData));

        // Initialize preview panels (4x4 grid for max tetromino size)
        initializePreviewPanel(nextPiecePanel, 4, 4);
        initializePreviewPanel(holdPiecePanel, 4, 4);

        // Update preview panels with initial data
        updatePreviewPanels(brick);

        // Initialize game timer
        gameTimer = new GameTimer(eventListener, 
            () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        
        // Start the falling timer
        gameTimer.start();
    }

    private void updateBrickPanelPosition(ViewData brick) {
        if (isPause.getValue() == Boolean.TRUE) {
            return;
        }

        double cellWidth = BRICK_WIDTH + brickPanel.getHgap();
        double cellHeight = BRICK_SIZE + brickPanel.getVgap();

        // x is straightforward: column index
        double x = gamePanel.getLayoutX() + brick.getxPosition() * cellWidth;

        // y: subtract hidden rows so row 2 maps to visible row 0
        double y = gamePanel.getLayoutY() + (brick.getyPosition() - HIDDEN_ROWS) * cellHeight;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    public void refreshBrick(ViewData brick) {
        this.currentViewData = brick;
        // update brick shape colors
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }

        // and move it to the correct place over the main rectangle
        updateBrickPanelPosition(brick);

        // Update preview panels
        updatePreviewPanels(brick);
    }

    private void initializePreviewPanel(GridPane panel, int rows, int cols) {
        if (panel == null)
            return;

        panel.getChildren().clear();
        Rectangle[][] previewRectangles = new Rectangle[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle rect = new Rectangle(BRICK_WIDTH, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(9);
                rect.setArcWidth(9);
                previewRectangles[i][j] = rect;
                panel.add(rect, j, i);
            }
        }

        // Store the rectangles for later updates
        if (panel == nextPiecePanel) {
            nextPieceRectangles = previewRectangles;
        } else if (panel == holdPiecePanel) {
            holdPieceRectangles = previewRectangles;
        }
    }

    private void updatePreviewPanels(ViewData viewData) {
        if (viewData == null)
            return;

        // Update next piece preview
        int[][] nextBrickData = viewData.getNextBrickData();
        if (nextBrickData != null && nextPieceRectangles != null) {
            updatePreviewPanel(nextPieceRectangles, nextBrickData);
        }

        // Update hold piece preview
        int[][] holdBrickData = viewData.getHoldBrickData();
        if (holdBrickData != null && holdPieceRectangles != null) {
            updatePreviewPanel(holdPieceRectangles, holdBrickData);
        } else if (holdPieceRectangles != null) {
            // Clear hold panel if no piece is held
            clearPreviewPanel(holdPieceRectangles);
        }
    }

    private void updatePreviewPanel(Rectangle[][] previewRectangles, int[][] brickData) {
        // Clear all rectangles first
        clearPreviewPanel(previewRectangles);

        if (brickData == null)
            return;

        // Center the piece in the 4x4 grid
        int offsetRow = (4 - brickData.length) / 2;
        int offsetCol = (4 - brickData[0].length) / 2;

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                int targetRow = i + offsetRow;
                int targetCol = j + offsetCol;
                if (targetRow >= 0 && targetRow < 4 && targetCol >= 0 && targetCol < 4) {
                    previewRectangles[targetRow][targetCol].setFill(getFillColor(brickData[i][j]));
                }
            }
        }
    }

    private void clearPreviewPanel(Rectangle[][] previewRectangles) {
        for (int i = 0; i < previewRectangles.length; i++) {
            for (int j = 0; j < previewRectangles[i].length; j++) {
                previewRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    public void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel(
                        "+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void hardDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel(
                        "+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
                // Refresh background when rows are cleared - we need to get this from the board
                // The GameController handles this internally, so we don't need to refresh here
            }
            // After hard drop, we want to show the *new* active brick
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

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
    }

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


    public void gameOver() {
        gameTimer.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        inputHandler.setGameOver(true);
    }

    public void newGame(ActionEvent actionEvent) {
        gameTimer.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        gameTimer.start();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        inputHandler.setPaused(false);
        inputHandler.setGameOver(false);
    }

    public void pauseGame(ActionEvent actionEvent) {
        // Do nothing if the game is already over
        if (isGameOver.getValue() == Boolean.TRUE) {
            return;
        }

        // Toggle pause state
        if (isPause.getValue() == Boolean.FALSE) {
            // Currently running → pause it
            gameTimer.pause();
            isPause.setValue(Boolean.TRUE);
            inputHandler.setPaused(true);
            if (pauseButton != null) {
                pauseButton.setText("Resume");
            }
        } else {
            // Currently paused → resume it
            gameTimer.resume();
            isPause.setValue(Boolean.FALSE);
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
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
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

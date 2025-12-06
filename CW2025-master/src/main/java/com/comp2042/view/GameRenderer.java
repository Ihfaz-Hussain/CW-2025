package com.comp2042.view;

import com.comp2042.model.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GameRenderer {

    private static final int BRICK_SIZE = 30;
    private static final int BRICK_WIDTH = 33;
    private static final int HIDDEN_ROWS = 2; // rows at the top we don’t show

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane nextPiecePanel;
    private final GridPane holdPiecePanel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] nextPieceRectangles;
    private Rectangle[][] holdPieceRectangles;

    public GameRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextPiecePanel, GridPane holdPiecePanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.nextPiecePanel = nextPiecePanel;
        this.holdPiecePanel = holdPiecePanel;
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // Create rectangle grid for the visible rows (board rows 2..end)
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        int visibleRows = boardMatrix.length - HIDDEN_ROWS;
        int columns = boardMatrix[0].length;

        // Set explicit size for gamePanel to eliminate padding/deadzone
        double prefWidth = columns * BRICK_WIDTH + (columns - 1) * gamePanel.getHgap();
        double prefHeight = visibleRows * BRICK_SIZE + (visibleRows - 1) * gamePanel.getVgap();
        gamePanel.setPrefWidth(prefWidth);
        gamePanel.setPrefHeight(prefHeight);
        gamePanel.setMinWidth(prefWidth);
        gamePanel.setMinHeight(prefHeight);
        gamePanel.setMaxWidth(prefWidth);
        gamePanel.setMaxHeight(prefHeight);

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

        // Initialize preview panels (4x4 grid for max tetromino size)
        initializePreviewPanel(nextPiecePanel, 4, 4);
        initializePreviewPanel(holdPiecePanel, 4, 4);

        // Update preview panels with initial data
        updatePreviewPanels(brick);
    }

    public void updateBrickPanelPosition(ViewData brick) {
        double cellWidth = BRICK_WIDTH + brickPanel.getHgap();
        double cellHeight = BRICK_SIZE + brickPanel.getVgap();

        // x is straightforward: column index
        double x = gamePanel.getLayoutX() + brick.getxPosition() * cellWidth;

        // y: subtract hidden rows so row 2 maps to visible row 0
        double y = gamePanel.getLayoutY() + (brick.getyPosition() - HIDDEN_ROWS) * cellHeight;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    public void refreshBrick(ViewData brick) {
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

    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
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

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
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
}

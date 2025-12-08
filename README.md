# Tetris - COMP2042 CW

## GitHub
[Link to your GitHub repository here]

## Compilation Instructions
1. Ensure you have **Java 17+** and **Maven** installed.
2. Open a terminal in the project root directory (where `pom.xml` is located).
3. Run the following command to compile and run the application:
   ```bash
   mvn clean javafx:run
   ```
4. The application window should launch automatically.

## Implemented and Working Properly
- **Classic Tetris Gameplay**: Complete implementation of Tetris rules, including movement, rotation, line clearing, and scoring.
- **MVC Architecture**: Refactored the monolithic codebase into Model-View-Controller pattern for better separation of concerns and maintainability.
- **Ghost/Shadow Piece**: Shows a semi-transparent preview of where the current brick will land, aiding player precision.
- **Hold Piece**: Allows players to hold a brick for later use (standard modern Tetris mechanic).
- **Next Piece Preview**: Displays the upcoming piece to help players plan their strategy.
- **Game Over Detection**: Correctly detects game over conditions both when spawning is blocked and when bricks lock above the visible board.
- **High Score System**: Tracks and saves high scores locally.
- **Pause Functionality**: Allows pausing and resuming the game.
- **Background Music**: Integrated audio management with playback controls.

## Implemented but Not Working Properly
- **None**: All implemented features are currently functioning as expected.

## Features Not Implemented
- **None**: successfully implemented all planned features.

## New Java Classes
- **`com.comp2042.model.Board`**: Interface defining the contract for board operations, facilitating modularity and testing.
- **`com.comp2042.model.SimpleBoard`**: Concrete implementation of the `Board` interface, encapsulating core game logic like movement, collision detection (`isBrickInHiddenRows`), and merging.
- **`com.comp2042.model.ViewData`**: Immutable data carrier object (DTO) used to pass game state (bricks, positions, ghost position) from Model to View without exposing internal logic.
- **`com.comp2042.model.InputEventListener`**: Interface used by the Controller to listen for and handle user input events in a decoupled way.
- **`com.comp2042.model.Score`**: Model class managing score state, line counts, and level logic.
- **`com.comp2042.audio.AudioManager`**: dedicated class for handling background music playback using Java Sound API.
- **`com.comp2042.view.GameRenderer`**: Dedicated rendering class responsible for drawing the board, active brick, ghost piece, and preview panels (next/hold). Features responsive design, color mapping for 7 tetromino types, and efficient grid management using JavaFX GridPane.
- **`com.comp2042.view.GameTimer`**: Handles the game loop timing and animation frame updates. Manages brick falling speed with level progression (400ms base interval, 20% speed increase per level capped at 4x), pause/resume functionality, and uses JavaFX Timeline for smooth timing.
- **`com.comp2042.view.InputHandler`**: Comprehensive keyboard input management system supporting both arrow keys and WASD controls. Maps keys to game actions (movement, rotation, soft/hard drop, hold), respects pause/game over states, and integrates with the event system for decoupled input handling.

## Modified Java Classes
- **`com.comp2042.controller.GameController`**: 
  - **Changes**: Refactored to act as the central coordinator in MVC. Removed direct rendering logic and instead delegates to `GameRenderer` and `SimpleBoard`. Added logic for ghost piece handling (`isBrickInHiddenRows`) and game over checks.
  - **Reason**: To separate game logic from UI code and ensure a clean architecture.
  
- **`com.comp2042.view.GuiController`**:
  - **Changes**: Updated to work with the new `Board` interface and `ViewData` snapshot. Added initialization for `GameRenderer`.
  - **Reason**: To support the new rendering pipeline and decoupled data flow.

- **`com.comp2042.controller.MenuController`**:
  - **Changes**: implementation of menu navigation and clean up unused imports.
  - **Reason**: To provide a functional entry point to the game.

## Unexpected Problems
- **Layout Alignment Issues**: When initially refactoring the renderer, the game board's alignment with the background image was slightly off, causing visual artifacts.
  - **Resolution**: Adjusted the `GridPane` constraints and padding in the FXML and ensured `GameRenderer` calculated positions accounting for hidden rows accurately.
- **Ghost Piece Rendering**: Adding the ghost piece initially caused some z-indexing issues where it would cover the active brick.
  - **Resolution**: updated the `initGhostPanel` method to explicitly insert the ghost panel behind the brick panel in the scene graph.
- **Game Over Detection**: The game originally only checked for game over on spawn collision, missing cases where bricks locked at the very top.
  - **Resolution**: Implemented `isBrickInHiddenRows()` to detect when a locked brick protrudes into the top non-visible rows and updated the game loop to check this condition.

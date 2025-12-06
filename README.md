# TetrisJFX - JavaFX Tetris Game

A modern Tetris implementation built with JavaFX, featuring clean object-oriented design and modular architecture.

## Features

- Classic Tetris gameplay with all 7 tetromino pieces
- Keyboard controls (Arrow keys, WASD, Space for hard drop, Shift/C for hold)
- Score tracking with level progression
- High score system with player name tracking
- Pause/Resume functionality
- Music toggle
- Next piece and hold piece previews
- Responsive UI design
- Game over and restart functionality

## Project Structure

```
src/main/java/com/comp2042/
├── Main.java                    # Application entry point
├── audio/
│   └── AudioManager.java        # Background music management
├── controller/
│   ├── GameController.java      # Game logic controller
│   ├── HighScoreController.java # High score screen controller
│   ├── MenuController.java      # Main menu controller
│   └── PlayerNameController.java # Player name input controller
├── event/
│   ├── EventSource.java         # Event source enumeration
│   ├── EventType.java           # Event type enumeration
│   ├── InputEventListener.java  # Input event listener interface
│   └── MoveEvent.java           # Move event data class
├── logic/
│   ├── BrickRotator.java        # Brick rotation logic
│   ├── MatrixOperations.java    # Matrix manipulation utilities
│   └── bricks/
│       ├── Brick.java           # Brick interface
│       ├── BrickGenerator.java  # Brick generator interface
│       ├── RandomBrickGenerator.java # Random brick generator
│       ├── IBrick.java          # I-piece implementation
│       ├── JBrick.java          # J-piece implementation
│       ├── LBrick.java          # L-piece implementation
│       ├── OBrick.java          # O-piece implementation
│       ├── SBrick.java          # S-piece implementation
│       ├── TBrick.java          # T-piece implementation
│       └── ZBrick.java          # Z-piece implementation
├── model/
│   ├── Board.java               # Board interface
│   ├── ClearRow.java            # Row clearing data class
│   ├── DownData.java            # Down movement data class
│   ├── HighScoreEntry.java      # High score entry data class
│   ├── HighScoreManager.java    # High score management
│   ├── NextShapeInfo.java       # Next shape information
│   ├── Score.java               # Score tracking with JavaFX properties
│   ├── SimpleBoard.java         # Board implementation
│   └── ViewData.java            # View data transfer object
└── view/
    ├── GameOverPanel.java       # Game over UI component
    ├── GuiController.java       # Main game UI controller (refactored)
    ├── GameTimer.java           # Game timing management (extracted)
    ├── InputHandler.java        # Keyboard input handling (extracted)
    └── NotificationPanel.java   # Score notification UI component
```

## Recent Refactoring (OOP Principles Applied)

### **Single Responsibility Principle (SRP)**

**Before**: The `GuiController` class (624 lines) handled multiple responsibilities:
- UI rendering and layout management
- Game timing and timeline control
- Keyboard input handling and mapping
- Audio management coordination
- Scene navigation

**After**: Extracted focused classes:

#### **GameTimer Class**
- **Responsibility**: Manages game timeline, speed updates, and pause/resume functionality
- **Benefits**: 
  - Isolated timing logic for easier testing
  - Clean separation of game flow control
  - Reusable timing component

```java
public class GameTimer {
    public void start();
    public void stop();
    public void pause();
    public void resume();
    public void updateSpeedForLevel(int level);
}
```

#### **InputHandler Class**
- **Responsibility**: Handles all keyboard input mapping and execution
- **Benefits**:
  - Centralized key binding management
  - Easy to extend with new controls
  - Independent input validation

```java
public class InputHandler {
    public void setEventListener(InputEventListener eventListener);
    public void setPaused(boolean paused);
    public void setGameOver(boolean gameOver);
    public Runnable getActionForKey(KeyCode keyCode);
}
```

### 🔧 **Open/Closed Principle (OCP)**

**Improvement**: The refactored design allows for extension without modification:
- `GameTimer` can be extended for different timing strategies
- `InputHandler` supports adding new key bindings dynamically
- UI components can be swapped without changing core logic

### **Dependency Inversion Principle (DIP)**

**Before**: Direct dependencies and tight coupling
```java
// Old approach - direct control
timeline = new Timeline(new KeyFrame(...));
keyActions = new HashMap<>();
```

**After**: Dependency injection and abstraction
```java
// New approach - composition
gameTimer = new GameTimer(eventListener, onTickAction);
inputHandler = new InputHandler();
```

###**Refactoring Impact**

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| GuiController Lines | 624 | ~580 | -7% |
| Classes | 1 monolithic | 3 focused | Better cohesion |
| Responsibilities | 6+ | 1 per class | Single responsibility |
| Testability | Poor | Good | Isolated components |
| Maintainability | Difficult | Easy | Clear separation |

###**Benefits Achieved**

1. **Better Testability**: `GameTimer` and `InputHandler` can be unit tested independently
2. **Improved Maintainability**: Changes to timing or input logic don't affect UI rendering
3. **Enhanced Reusability**: Extracted classes can be reused in other game components
4. **Cleaner Code**: Removed unused imports and redundant methods
5. **Better Encapsulation**: Related functionality is properly grouped and hidden

## How to Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Build and Run
```bash
# Clone the repository
git clone <repository-url>
cd CW2025-master

# Build the project
mvn clean compile

# Run the application
mvn javafx:run
```

Or run directly from IDE:
1. Open the project in IntelliJ IDEA or Eclipse
2. Run the `Main.java` class

## Controls

| Action | Keys |
|--------|------|
| Move Left | ← / A |
| Move Right | → / D |
| Rotate | ↑ / W |
| Soft Drop | ↓ / S |
| Hard Drop | Space |
| Hold Piece | Shift / C |
| Pause | Pause Button |
| Restart | Restart Button |
| Toggle Music | Music Button |
| Return to Menu | Menu Button |

## Scoring System

- **Single Line**: 100 points
- **Double Lines**: 300 points
- **Triple Lines**: 500 points
- **Tetris (4 lines)**: 800 points
- **Soft Drop**: 1 point per cell
- **Level Progression**: Every 5 lines cleared increases level by 1
- **Speed Increase**: Each level increases falling speed by 20%

## Architecture Patterns

The project follows several design patterns:

- **Model-View-Controller (MVC)**: Clear separation between game logic, UI, and data
- **Observer Pattern**: JavaFX properties for reactive UI updates
- **Strategy Pattern**: Different brick types with unified interface
- **Factory Pattern**: Brick generation system
- **Command Pattern**: Event-driven input handling

## Future Enhancements

Potential areas for further OOP improvements:

1. **Strategy Pattern for Scoring**: Different scoring systems (classic, modern, custom)
2. **Abstract Factory for Themes**: Different visual themes and color schemes
3. **Command Pattern for Actions**: Undo/redo functionality
4. **Observer Pattern for Game Events**: Better event system for sound effects and animations
5. **State Pattern for Game States**: Menu, playing, paused, game over states

## Contributing

When contributing to this project, please follow the established OOP principles:

1. Keep classes focused on a single responsibility
2. Program to interfaces, not implementations
3. Favor composition over inheritance
4. Keep coupling low and cohesion high
5. Write unit tests for new components

## License

This project is for educational purposes to demonstrate OOP principles and JavaFX development.

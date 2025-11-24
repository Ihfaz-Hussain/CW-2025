import com.comp2042.HighScoreManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class HighScoreManagerTest {
    // Path where your game stores highscores
    private static final Path HIGH_SCORE_PATH = Path.of("highscores.txt");

    @AfterEach
    void cleanUp() throws IOException {
        if (Files.exists(HIGH_SCORE_PATH)) {
            Files.delete(HIGH_SCORE_PATH);
        }
    }

    //Test 1: No highscore
    @Test
    void testNoHighScore(){
        // LOAD without saving anything first
        List<Integer> scores = HighScoreManager.loadHighScores();

        // Expect list is empty
        assertTrue(scores.isEmpty(), "No highscores should return an empty list");
    }

    // Test 2: Exactly 3 highscores
    @Test
     void testThreeHighScores(){
         HighScoreManager.recordScore(100);
         HighScoreManager.recordScore(300);
         HighScoreManager.recordScore(200);
         //load and check order of highscore
         List<Integer> scores = HighScoreManager.loadHighScores();

         assertEquals(3, scores.size(), "3 scores expected");
         assertEquals(300, scores.get(0));
         assertEquals(200, scores.get(1));
         assertEquals(100, scores.get(2));
     }
     @Test
     void testFourHighScores(){
        //record 4 scores
        HighScoreManager.recordScore(500);
        HighScoreManager.recordScore(100);
        HighScoreManager.recordScore(300);
        HighScoreManager.recordScore(400);
        //load the saved scores
        List<Integer> scores = HighScoreManager.loadHighScores();

        //top 3 scores in ascending]
         assertEquals(3, scores.size(), "Leaderboard must contain only top 3 scores");
         assertEquals(500, scores.get(0));
         assertEquals(400, scores.get(1));
         assertEquals(300, scores.get(2));
    }

    @Test
    void zeroInclusiveScores(){
        HighScoreManager.recordScore(0);
        HighScoreManager.recordScore(200);
        HighScoreManager.recordScore(100);

        List<Integer> scores = HighScoreManager.loadHighScores();

        //sorted highest to lowest
        assertEquals(3, scores.size());
        assertEquals(200, scores.get(0));
        assertEquals(100, scores.get(1));
        assertEquals(0, scores.get(2));
    }
}

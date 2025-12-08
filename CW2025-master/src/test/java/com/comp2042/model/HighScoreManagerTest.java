package com.comp2042.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HighScoreManagerTest {

    private static final Path HIGH_SCORE_PATH = Paths.get("highscores.txt");
    private byte[] originalContent = null;
    private boolean fileExisted = false;

    @BeforeEach
    void setUp() throws IOException {
        // Backup existing file if it exists
        if (Files.exists(HIGH_SCORE_PATH)) {
            fileExisted = true;
            originalContent = Files.readAllBytes(HIGH_SCORE_PATH);
        }
        // Clear or delete the file for a clean test
        Files.deleteIfExists(HIGH_SCORE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore original file if it existed
        if (fileExisted && originalContent != null) {
            Files.write(HIGH_SCORE_PATH, originalContent);
        } else {
            Files.deleteIfExists(HIGH_SCORE_PATH);
        }
    }

    // 1. Test loading returns empty list when no file exists
    @Test
    void testLoadHighScoresReturnsEmptyWhenNoFile() {
        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertTrue(scores.isEmpty(), "Should return empty list when no file exists");
    }

    // 2. Test recording a single score
    @Test
    void testRecordSingleScore() {
        HighScoreManager.recordScore("Alice", 100);

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertEquals(1, scores.size(), "Should have 1 entry");
        assertEquals("Alice", scores.get(0).getPlayerName());
        assertEquals(100, scores.get(0).getScore());
    }

    // 3. Test scores are sorted in descending order
    @Test
    void testScoresSortedDescending() {
        HighScoreManager.recordScore("Low", 50);
        HighScoreManager.recordScore("High", 200);
        HighScoreManager.recordScore("Mid", 100);

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertEquals(3, scores.size());
        assertEquals(200, scores.get(0).getScore(), "Highest score should be first");
        assertEquals(100, scores.get(1).getScore(), "Middle score should be second");
        assertEquals(50, scores.get(2).getScore(), "Lowest score should be third");
    }

    // 4. Test only top 3 scores are kept
    @Test
    void testOnlyTop3ScoresKept() {
        HighScoreManager.recordScore("A", 10);
        HighScoreManager.recordScore("B", 20);
        HighScoreManager.recordScore("C", 30);
        HighScoreManager.recordScore("D", 40); // This should push out "A" (10)

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertEquals(3, scores.size(), "Should only keep 3 scores");
        assertEquals(40, scores.get(0).getScore());
        assertEquals(30, scores.get(1).getScore());
        assertEquals(20, scores.get(2).getScore());
    }

    // 5. Test legacy recordScore with just integer uses "Anonymous"
    @Test
    @SuppressWarnings("deprecation")
    void testLegacyRecordScoreUsesAnonymous() {
        HighScoreManager.recordScore(999);

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertEquals(1, scores.size());
        assertEquals("Anonymous", scores.get(0).getPlayerName());
        assertEquals(999, scores.get(0).getScore());
    }

    // 6. Test loadHighScoresAsIntegers returns only scores
    @Test
    @SuppressWarnings("deprecation")
    void testLoadHighScoresAsIntegers() {
        HighScoreManager.recordScore("X", 300);
        HighScoreManager.recordScore("Y", 200);

        List<Integer> integerScores = HighScoreManager.loadHighScoresAsIntegers();
        assertEquals(2, integerScores.size());
        assertEquals(300, integerScores.get(0));
        assertEquals(200, integerScores.get(1));
    }

    // 7. Test that equal scores are handled (stable ordering)
    @Test
    void testEqualScoresHandled() {
        HighScoreManager.recordScore("First", 100);
        HighScoreManager.recordScore("Second", 100);

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        assertEquals(2, scores.size());
        // Both should be present with score 100
        assertTrue(scores.stream().allMatch(e -> e.getScore() == 100));
    }

    // 8. Test HighScoreEntry compareTo
    @Test
    void testHighScoreEntryComparison() {
        HighScoreEntry low = new HighScoreEntry("Low", 50);
        HighScoreEntry high = new HighScoreEntry("High", 100);

        assertTrue(low.compareTo(high) > 0, "Low should come after High when sorted");
        assertTrue(high.compareTo(low) < 0, "High should come before Low when sorted");
        assertEquals(0, low.compareTo(new HighScoreEntry("Same", 50)), "Equal scores compare as 0");
    }

    // 9. Test HighScoreEntry toString format
    @Test
    void testHighScoreEntryToString() {
        HighScoreEntry entry = new HighScoreEntry("Player1", 1234);
        assertEquals("Player1: 1234", entry.toString());
    }

    // 10. Test loading malformed file lines are skipped
    @Test
    void testMalformedLinesSkipped() throws IOException {
        // Write a file with some valid and some invalid lines
        List<String> lines = List.of(
                "ValidPlayer:500",
                "InvalidLineNoColon",
                "AnotherValid:300",
                "BadScore:abc", // Not a number
                ":100" // Empty name but valid score
        );
        Files.write(HIGH_SCORE_PATH, lines);

        List<HighScoreEntry> scores = HighScoreManager.loadHighScores();
        // Should only have 3 valid entries: ValidPlayer:500, AnotherValid:300, :100
        // (empty name is technically valid)
        assertEquals(3, scores.size(), "Should parse valid lines only");
        assertEquals(500, scores.get(0).getScore());
        assertEquals(300, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }
}

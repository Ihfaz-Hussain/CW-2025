package com.comp2042.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Path;

public class HighScoreManager {
    private static final Path HIGH_SCORE_PATH = Paths.get("highscores.txt");

    // Read high scores from the file
    public static List<HighScoreEntry> loadHighScores() {
        List<HighScoreEntry> entries = new ArrayList<>();

        // If the file does not exist yet → no scores saved
        if (!Files.exists(HIGH_SCORE_PATH)) {
            return entries;
        }

        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(HIGH_SCORE_PATH);

            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        // Parse format: "PlayerName:Score"
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            String name = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim());
                            entries.add(new HighScoreEntry(name, score));
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                        // If line format is invalid, just skip it
                    }
                }
            }

            // Sort entries by score (highest first) - HighScoreEntry implements Comparable
            entries.sort(null);

            // If there are more than 3 scores, keep only the first 3
            if (entries.size() > 3) {
                entries = new ArrayList<>(entries.subList(0, 3));
            }

        } catch (IOException e) {
            // If any file reading error happens, just return empty list
            entries.clear();
        }

        return entries;
    }

    // Record a new score with player name
    public static void recordScore(String playerName, int score) {
        List<HighScoreEntry> entries = loadHighScores();

        // Add the new entry
        entries.add(new HighScoreEntry(playerName, score));

        // Sort by score (highest first)
        entries.sort(null);

        // Keep only top 3
        if (entries.size() > 3) {
            entries = new ArrayList<>(entries.subList(0, 3));
        }

        // Save updated entries to file
        saveHighScores(entries);
    }

    // Legacy method for backward compatibility
    public static void recordScore(int score) {
        recordScore("Anonymous", score);
    }

    // Legacy method for backward compatibility
    public static List<Integer> loadHighScoresAsIntegers() {
        return loadHighScores().stream()
                .map(HighScoreEntry::getScore)
                .collect(Collectors.toList());
    }

    // Save high score entries to file
    private static void saveHighScores(List<HighScoreEntry> entries) {
        List<String> lines = entries.stream()
                .map(entry -> entry.getPlayerName() + ":" + entry.getScore())
                .collect(Collectors.toList());

        try {
            Files.write(HIGH_SCORE_PATH, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

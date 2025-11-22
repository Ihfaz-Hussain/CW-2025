package com.comp2042;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Path;

public class HighScoreManager {
    private static final Path HIGH_SCORE_PATH = Paths.get("highscores.txt");
    //read highscores from the txt file
    public static List<Integer> loadHighScores() {
        List<Integer> scores = new ArrayList<>();

        // If the file does not exist yet → no scores saved
        if (!Files.exists(HIGH_SCORE_PATH)) {
            return scores;
        }

        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(HIGH_SCORE_PATH);

            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        // Convert text like "1200" to int 1200
                        scores.add(Integer.parseInt(line));
                    } catch (NumberFormatException ignored) {
                        // If line is not a number, just skip it
                    }
                }
            }

            // Sort scores: highest first
            scores.sort(Comparator.reverseOrder());

            // If there are more than 3 scores, keep only the first 3
            if (scores.size() > 3) {
                scores = new ArrayList<>(scores.subList(0, 3));
            }

        } catch (IOException e) {
            // If any file reading error happens, just return empty list
            scores.clear();
        }

        return scores;
    }

    public static void recordScore(int newScore) {
        List<Integer> scores = loadHighScores();
        // Add the new score
        scores.add(newScore);
        //Descending order of highscores kept
        scores.sort(Comparator.reverseOrder());
        //Top three highscores kept
        if (scores.size() > 3) {
            scores = new ArrayList<>(scores.subList(0, 3));
        }
        // Save updated scores to file
        saveHighScores(scores);
    }
    //save new highscore if its greater than the current
    private static void saveHighScores(List<Integer> scores) {
        List<String> lines = scores.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        try {
            Files.write(HIGH_SCORE_PATH, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package com.comp2042.model;

public class HighScoreEntry implements Comparable<HighScoreEntry> {
    private final String playerName;
    private final int score;
    
    public HighScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public int getScore() {
        return score;
    }
    
    @Override
    public int compareTo(HighScoreEntry other) {
        // Sort by score in descending order (higher scores first)
        return Integer.compare(other.score, this.score);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %d", playerName, score);
    }
}

package com.comp2042.model;

/**
 * Represents a single high score entry with player name and score.
 * Implements Comparable to enable sorting by score in descending order.
 * Instances are immutable once created.
 */
public class HighScoreEntry implements Comparable<HighScoreEntry> {
    private final String playerName;
    private final int score;
    
    /**
     * Creates a new high score entry.
     *
     * @param playerName the name of the player who achieved the score
     * @param score      the score value achieved
     */
    public HighScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
    
    /**
     * Gets the player name associated with this high score.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Gets the score value for this entry.
     *
     * @return the score value
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Compares this high score entry to another for ordering.
     * Entries are sorted in descending order by score (higher scores first).
     *
     * @param other the other high score entry to compare to
     * @return negative if this score is higher, positive if lower, zero if equal
     */
    @Override
    public int compareTo(HighScoreEntry other) {
        // Sort by score in descending order (higher scores first)
        return Integer.compare(other.score, this.score);
    }
    
    /**
     * Returns a string representation of this high score entry.
     * Format: "PlayerName: Score"
     *
     * @return the formatted string representation
     */
    @Override
    public String toString() {
        return String.format("%s: %d", playerName, score);
    }
}

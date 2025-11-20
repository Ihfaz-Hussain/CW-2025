    package com.comp2042;

    import javafx.beans.property.IntegerProperty;
    import javafx.beans.property.SimpleIntegerProperty;

    public final class Score {

        //current score shown on screen
        private final IntegerProperty score = new SimpleIntegerProperty(0);
        //Total number of lines cleared so far in the game
        private final IntegerProperty totalLines  = new SimpleIntegerProperty(0);
        // Current level (starts at level 1)
        private final IntegerProperty level = new SimpleIntegerProperty(1);
        //scoring
        public IntegerProperty scoreProperty() {
            return score;
        }

        public void add(int i){
            //Increase score by i
            score.setValue(score.getValue() + i);
        }

        //Lines and level


        public IntegerProperty levelProperty() {
            return level;
        }

        public IntegerProperty totalLinesProperty() {
            return totalLines;
        }

        //Whenever lines ie cleared add total lines cleared
        //Updates the level based on the total lines cleared

        public void addLines(int lines) {
            if (lines <= 0) return;

            int newTotal = totalLines.get() + lines;
            totalLines.set(newTotal);

            //every 5 lines = +1 level
            int newLevel = 1 + (newTotal / 5);
            // Do not allow level to drop below 1
            if (newLevel < 1) newLevel = 1;
            level.set(newLevel);
        }

        public void reset() {
            score.setValue(0);
            totalLines.setValue(0);
            level.setValue(1);
        }
    }

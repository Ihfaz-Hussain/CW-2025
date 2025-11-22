package com.comp2042;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    //read highscores from the txt file
    public static int loadHighScore(){
        Path path = Paths.get(HIGH_SCORE_FILE);
        //if there is no file yet, return 0 to still run the program
        if (!Files.exists(path)) {
            return 0;
        }

        //error handling
        try{
            String text = Files.readString(path).trim();
            return Integer.parseInt(text);
        }catch(IOException| NumberFormatException e){
            //if there's error reading the highscore return highscore as 0
            return 0;

        }
    }
    //save new highscore if its greater than the current
    public static void saveHighScore(int newScore){
        int currentHighScore = loadHighScore();

        if(newScore > currentHighScore){
            try{
                Files.writeString(Paths.get(HIGH_SCORE_FILE), String.valueOf(newScore));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


}

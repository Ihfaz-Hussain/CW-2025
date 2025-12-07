package com.comp2042.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class BoardShake {
    public static void play(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setByX(10);
        tt.setCycleCount(2);
        tt.setAutoReverse(true);
        tt.play();
    }
}

package com.example.demo.utils;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Transitions {
    public static FillTransition createHighlighter(Shape node, Color startColor, Color endColor, Integer duration) {
        FillTransition ft = new FillTransition();
        ft.setShape(node);
        ft.setFromValue(startColor);
        ft.setToValue(endColor);
        ft.setInterpolator(Interpolator.EASE_IN);
        if(duration != null) {
            ft.setDuration(Duration.millis(duration));
        } else {
            ft.setDuration(Duration.millis(300));
        }
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        return ft;
    }

    public static FillTransition createHighlighter(Shape node, String blockType, Integer duration) {
        if(blockType == "code") {
            return createHighlighter(node, Color.INDIGO, Color.BLACK, duration);
        } else {
            return createHighlighter(node, Color.ORANGE, Color.RED, duration);
        }
    }
    static TranslateTransition translateX(Node node, double start, double end, double duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(start);
        translateTransition.setToX(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    static TranslateTransition translateY(Node node, double start, double end, double duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromY(start);
        translateTransition.setToY(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }
}

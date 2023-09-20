package com.example.demo.utils;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Transitions {
    private static final Duration defaultTranslationDuration = Duration.millis(300);
    private static final Duration defaultFillDuration = Duration.millis(400);

    public static FillTransition createHighlighter(Shape node, Color startColor, Color endColor, Integer duration) {
        FillTransition ft = new FillTransition();
        ft.setShape(node);
        ft.setFromValue(startColor);
        ft.setToValue(endColor);
        ft.setInterpolator(Interpolator.EASE_IN);
        if(duration != null) {
            ft.setDuration(Duration.millis(duration));
        } else {
            ft.setDuration(defaultFillDuration);
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
    public static TranslateTransition translateX(Node node, double start, double end, Integer duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(start);
        translateTransition.setToX(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    public static TranslateTransition translateX(Node node, double distance, Integer duration) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(node);
        if(duration != null) {
            translateTransition.setDuration(Duration.millis(duration));
        } else {
            translateTransition.setDuration(defaultTranslationDuration);
        }
        translateTransition.setByX(distance);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    public static TranslateTransition translateY(Node node, double start, double end, double duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromY(start);
        translateTransition.setToY(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    public static FadeTransition fadeItemIn(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), node);
        fadeTransition.setCycleCount(1);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        return fadeTransition;
    }

    public static FadeTransition fadeItemOut(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), node);
        fadeTransition.setCycleCount(1);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        return fadeTransition;
    }
}

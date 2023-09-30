package com.example.demo.components;

import javafx.animation.FillTransition;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class SpeedSlider {
    private final double MIN_SPEED = 0.25;
    private final double MID_SPEED = 1.75;
    private final double MAX_SPEED = 3;

    private Slider speedSlider;
    public SpeedSlider() {
        // Create a slider for animation speed control
        speedSlider = new Slider(MIN_SPEED, MAX_SPEED, MID_SPEED);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(0.25);

        speedSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n == MAX_SPEED) {
                    return "Fast";
                } else if (n == MID_SPEED) {
                    return "Normal";
                } else if (n == MIN_SPEED) {
                    return "Slow";
                } else {
                    return ""; // Return an empty string for other values
                }
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
    }

    public Slider getSpeedSlider() {
        return speedSlider;
    }

}

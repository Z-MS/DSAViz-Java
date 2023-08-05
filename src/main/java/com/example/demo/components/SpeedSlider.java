package com.example.demo.components;

import javafx.animation.FillTransition;
import javafx.scene.control.Slider;
import javafx.util.Duration;

public class SpeedSlider extends Slider {
    private final Duration MIN_DURATION = Duration.seconds(0.5);
    private final Duration MAX_DURATION = Duration.seconds(2);

    private Slider speedSlider;
    public SpeedSlider() {
        // Create a slider for animation speed control
        speedSlider = new Slider(0.25, 2, 1);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setBlockIncrement(0.5);

    }

    public Slider getSpeedSlider() {
        return speedSlider;
    }

    public void setAnimationSpeed(FillTransition fillTransition, double speedFactor) {
        Duration duration = Duration.seconds(1.0 / speedFactor);
        if (duration.compareTo(MAX_DURATION) > 0) {
            duration = MAX_DURATION;
        } else if (duration.compareTo(MIN_DURATION) < 0) {
            duration = MIN_DURATION;
        }
        fillTransition.setDuration(duration);
    }
}

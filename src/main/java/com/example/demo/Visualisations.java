package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.SpeedSlider;
import com.example.demo.utils.PlayingQueue;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Queue;

public abstract class Visualisations {
    Pane pane = new Pane();
    protected Scene scene = new Scene(pane, 1366, 500,Color.GHOSTWHITE);
    protected Group algoTracer = new Group();
    protected Group mainArea = new Group();
    protected final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
    protected HBox controls = new HBox();
    protected HBox buttonContainer = new HBox();
    protected Button restartButton = new Button("Restart");
    protected Button newButton = new Button("New");
    protected Slider speedSlider;
    protected boolean resetFlag = false;
    protected ArrayList<Comparable> inputArray = new ArrayList<>();
    protected CharBlock[] charBlocks;
    protected ArrayList<Text> indexTexts = new ArrayList<>();
    protected ArrayList<Double> indexTextCentres = new ArrayList<>();
    protected ArrayList<FillTransition> charBlockAnims = new ArrayList<>();
    protected ArrayList<FillTransition> codeAnims = new ArrayList<>();
    protected ArrayList<TranslateTransition> translateTransitions = new ArrayList<>();
    protected ArrayList<FadeTransition> fadeTransitions = new ArrayList<>();
    protected ArrayList<Transition> allAnimations = new ArrayList<>();
    protected Queue<Transition> playingQueue = new PlayingQueue().getPlayingQueue();
    protected VBox algoTracerContainer;
    protected Group mainAreaContainer;
    protected TitledPane titledPane;
    protected final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
    protected final int indentY = (int) (pane.getLayoutBounds().getCenterY()) - 50;
    protected abstract void initMainArea();

    protected abstract void initAlgoTracer();

    protected abstract void handleOnFinished();

    protected abstract void setAllAnimations();

    protected void setAllAnimationsSpeed() {
        double speedFactor = speedSlider.valueProperty().doubleValue();
        for (Transition animation : allAnimations) {
            animation.setRate(speedFactor);
        }
    }

    protected void initControls() {
        speedSlider = new SpeedSlider().getSpeedSlider();
        speedSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double speedFactor = Math.abs(newValue.doubleValue());

            for (Transition animation : allAnimations) {
                animation.setRate(speedFactor);
            }
        });

        controls.getChildren().add(speedSlider);

        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);

        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);
        restartButton.setOnMouseClicked(e -> reset());

        newButton.setPrefWidth(150);
        newButton.setPrefHeight(20);
        newButton.setOnMouseClicked(e -> generateNew());

        buttonContainer.getChildren().addAll(restartButton, newButton);
        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);
    }

    protected void addToPane() {
        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);
    }

    protected abstract void clear();

    protected abstract void generateNew() ;

    protected abstract void reset();

    protected void start() {
        codeAnims.get(0).play();
    }

    protected abstract void initVisualisation();

    public Visualisations() {
        mainArea.setLayoutX(20);
        initVisualisation();
    }

    public Scene getScene() {
        return scene;
    }
}

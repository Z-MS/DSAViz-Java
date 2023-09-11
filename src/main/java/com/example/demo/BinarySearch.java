package com.example.demo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BinarySearch {
    private Scene scene;

    public BinarySearch () {
        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        scene = new Scene(pane, 1366, 500, Color.GHOSTWHITE);

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);

        Button newSearchButton = new Button("New search");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);

        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer);
    }

    public Scene getScene() {
        return scene;
    }

}

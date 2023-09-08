package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LinearSearchController {
    @FXML
    Pane linearSearch;
    @FXML
    ToolBar toolbar;
    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    public void goHome(ActionEvent event) throws IOException {
        root = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void loadLinearSearch(Scene scene) {
        Pane newScene = (Pane) scene.getRoot();

        linearSearch.getChildren().add(newScene);
        double toolbarBottom = toolbar.getPrefHeight() + toolbar.getLayoutY();
        newScene.setLayoutY(toolbarBottom);
    }

}

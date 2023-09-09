package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Parent root;
    private FXMLLoader loader;
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void loadLinearSearch(Scene scene) {
        Pane newScene = (Pane) scene.getRoot();

        linearSearch.getChildren().add(newScene);
        double toolbarBottom = toolbar.getPrefHeight() + toolbar.getLayoutY();
        newScene.setLayoutY(toolbarBottom);
    }

}

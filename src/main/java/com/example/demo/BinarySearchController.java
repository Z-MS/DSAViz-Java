package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class BinarySearchController {
    @FXML
    Pane binarySearch;
    @FXML
    ToolBar toolbar;
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void loadBinarySearch(Scene scene) {
        Pane newScene = (Pane) scene.getRoot();

        binarySearch.getChildren().add(newScene);
        double toolbarBottom = toolbar.getPrefHeight() + toolbar.getLayoutY();
        newScene.setLayoutY(toolbarBottom);
    }
}

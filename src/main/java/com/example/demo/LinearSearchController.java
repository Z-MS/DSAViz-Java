package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LinearSearchController {
    @FXML
    Pane linearSearch;
    @FXML
    ToolBar toolbar;
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void goToLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "linear-search-lesson.fxml");
    }

    public void loadLinearSearch(Scene scene) {
        Pane newScene = (Pane) scene.getRoot();

        linearSearch.getChildren().add(newScene);
        double toolbarBottom = toolbar.getPrefHeight() + toolbar.getLayoutY();
        newScene.setLayoutY(toolbarBottom);
    }

}

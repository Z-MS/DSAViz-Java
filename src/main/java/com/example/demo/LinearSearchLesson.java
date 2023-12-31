package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LinearSearchLesson {
    @FXML
    ToolBar toolbar;

    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void goToVisualisation(ActionEvent event) throws IOException {
        NavigationHelper.enterLinearSearchVis(event);
    }
}

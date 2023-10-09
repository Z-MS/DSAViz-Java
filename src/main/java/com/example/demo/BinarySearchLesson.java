package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToolBar;

import java.io.IOException;

public class BinarySearchLesson {
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void goToVisualisation(ActionEvent event) throws IOException {
        NavigationHelper.enterBinarySearchVis(event);
    }
}

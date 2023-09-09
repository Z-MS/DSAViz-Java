package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.io.IOException;

public class LinearSearchLesson {
    @FXML
    ToolBar toolbar;
    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }
}

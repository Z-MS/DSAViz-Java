package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.demo.LinearSearch;

import java.io.IOException;

public class MainMenuController {
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private Parent root;

    public void enterLinearSearchVis(ActionEvent event) throws IOException {
        loader = new FXMLLoader(HelloApplication.class.getResource("linear-search.fxml"));
        root = loader.load();

        LinearSearchController linearSearchController = loader.getController();
        linearSearchController.loadLinearSearch(new LinearSearch().getScene());

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void enterLinearSearchLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "linear-search-lesson.fxml");
    }

    public void enterBinarySearchVis(ActionEvent event) throws IOException {
        loader = new FXMLLoader(HelloApplication.class.getResource("binary-search.fxml"));
        root = loader.load();

        BinarySearchController binarySearchController = loader.getController();
        binarySearchController.loadBinarySearch(new BinarySearch().getScene());

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
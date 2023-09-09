package com.example.demo.utils;

import com.example.demo.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationHelper {
    /*private Stage stage;
    private Scene scene;
    private FXMLLoader root;*/
    public static void goHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // go to page without passing any data
    public static void goToPage(ActionEvent event, String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(filename));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

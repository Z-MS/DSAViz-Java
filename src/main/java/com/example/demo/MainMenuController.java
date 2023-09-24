package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {
    public void enterLinearSearchVis(ActionEvent event) throws IOException {
        NavigationHelper.enterLinearSearchVis(event);
    }

    public void enterLinearSearchLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "linear-search-lesson.fxml");
    }

    public void enterBinarySearchVis(ActionEvent event) throws IOException {
        NavigationHelper.enterBinarySearchVis(event);
    }

    public void enterSelectionSortVis(ActionEvent event) throws IOException {
        NavigationHelper.enterSelectionSortVis(event);
    }
}
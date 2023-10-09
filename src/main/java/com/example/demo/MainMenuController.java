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

    public void enterBinarySearchLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "binary-search-lesson.fxml");
    }

    public void enterSelectionSortVis(ActionEvent event) throws IOException {
        NavigationHelper.enterSelectionSortVis(event);
    }

    public void enterSelectionSortLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "selection-sort-lesson.fxml");
    }

    public void enterQueueLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "queue-lesson.fxml");
    }

    public void enterStacksLesson(ActionEvent event) throws IOException {
        NavigationHelper.goToPage(event, "stacks-lesson.fxml");
    }
}
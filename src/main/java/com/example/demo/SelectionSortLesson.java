package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SelectionSortLesson {
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }

    public void goToVisualisation(ActionEvent event) throws IOException {
        NavigationHelper.enterSelectionSortVis(event);
    }
}

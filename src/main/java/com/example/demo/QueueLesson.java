package com.example.demo;

import com.example.demo.utils.NavigationHelper;
import javafx.event.ActionEvent;

import java.io.IOException;

public class QueueLesson {
    public void goHome(ActionEvent event) throws IOException {
        NavigationHelper.goHome(event);
    }
}

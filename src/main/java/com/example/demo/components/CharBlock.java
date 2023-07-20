package com.example.demo.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharBlock extends TextBlock {
    public CharBlock(double posX, double posY, double size, String text, Color color, int fontSize) {
        rect = new Rectangle(posX, posY, size, size);
        if(color == null) {
            rect.setFill(Color.ORANGE);
        }

        blockText = new Text(text);
        blockText.setFont(new Font("Consolas", fontSize));
        double textWidth = blockText.getLayoutBounds().getWidth();
        double textHeight = blockText.getLayoutBounds().getHeight();
        blockText.setX(rect.getLayoutBounds().getCenterX() - (textWidth / 2));
        blockText.setY(rect.getLayoutBounds().getCenterY() + (textHeight / 2.5));

        block = new Group(rect, blockText);
    }
}

package com.example.demo.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CodeBlock {
    private Text blockText;
    private Rectangle rect;
    private Group block;
    public CodeBlock(double posX, double posY, String text) {
//        block = new Rectangle(posX, posY);
        blockText = new Text(posX, posY, text);
        blockText.setFill(Color.WHEAT);
        blockText.setFont(new Font("Consolas", 20));

        rect = new Rectangle(posX, posY, blockText.getLayoutBounds().getWidth(),  blockText.getLayoutBounds().getHeight());
        rect.setFill(Color.INDIGO);

        blockText.relocate(rect.getX(), rect.getY());

        block = new Group();
        block.getChildren().add(rect);
        block.getChildren().add(blockText);
    }

    public Rectangle getRect() {
        return rect;
    } // For animation and positioning

    public Group getBlock() {
        return block;
    }
}

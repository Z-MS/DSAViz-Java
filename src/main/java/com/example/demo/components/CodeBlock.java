package com.example.demo.components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CodeBlock extends TextBlock {

    private double indent = 0;
    public CodeBlock(String text, double indent) {
        blockText = new Text(text);
        blockText.setFill(Color.WHEAT);
        blockText.setFont(new Font("Consolas", 20));

        double textWidth = blockText.getLayoutBounds().getWidth();
        double textHeight = blockText.getLayoutBounds().getHeight();

        rect = new Rectangle();
        rect.setWidth(textWidth);
        rect.setHeight(textHeight);
        rect.setFill(Color.INDIGO);

        blockText.relocate(rect.getX(), rect.getY());

        this.setIndent(indent);

        block = new Group();
        block.getChildren().add(rect);
        block.getChildren().add(blockText);
    }

    public CodeBlock(String text){
        this(text, 0);
    }
    public void setIndent(double indent) {
        this.indent = indent;
    }

    public double getIndent() {
        return indent;
    }
}

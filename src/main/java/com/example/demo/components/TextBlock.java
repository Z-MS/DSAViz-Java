package com.example.demo.components;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class TextBlock extends Group {
    protected Text blockText;
    protected Rectangle rect;
    protected Group block;

    public Rectangle getRect() {
        return rect;
    }

    public Text getBlockText() { return blockText; }
    public Group getBlock() {
        return block;
    }
}

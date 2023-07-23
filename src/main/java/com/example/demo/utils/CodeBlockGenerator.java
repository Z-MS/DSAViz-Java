package com.example.demo.utils;

import com.example.demo.components.CodeBlock;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class CodeBlockGenerator {
    private ArrayList<HBox> codeBlocks;

    public CodeBlockGenerator() {
        codeBlocks = new ArrayList<>();
    }

    public void addCodeBlock(CodeBlock codeBlock) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 0, 0, codeBlock.getIndent()));
        hBox.getChildren().add(codeBlock.getBlock());
        codeBlocks.add(hBox);
    }

    public void addCodeBlocks(ArrayList<CodeBlock> inputCodeBlocks) {
        HBox hBox = new HBox();
        // set padding according to the first element's indent value
        hBox.setPadding(new Insets(0, 0, 0, inputCodeBlocks.get(0).getIndent()));
        for(int i = 0; i < inputCodeBlocks.size(); i++) {
            CodeBlock currentCodeBlock = inputCodeBlocks.get(i);
            hBox.getChildren().add(currentCodeBlock.getBlock());
        }
        // Add the HBox to the end of the array
        codeBlocks.add(hBox);
    }

    public ArrayList<HBox> getCodeBlocks() {
        return codeBlocks;
    }
}

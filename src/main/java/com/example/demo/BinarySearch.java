package com.example.demo;

import com.example.demo.components.CodeBlock;
import com.example.demo.utils.CodeBlockGenerator;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class BinarySearch {
    private Scene scene;

    public BinarySearch () {
        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        scene = new Scene(pane, 1366, 500, Color.GHOSTWHITE);

        class Visualiser {
            CodeBlock methodDef, startendDeclaration, whileText, whileCondition, closingParen, divider, keyEquals, matchFound, keyLess, goLeft, elseText, goRight, noMatch;
            VBox algoTracerContainer;
            TitledPane titledPane;
            void initAlgoTracer() {
                CodeBlockGenerator generator = new CodeBlockGenerator();
                methodDef = new CodeBlock("binarySearch(array, key)");
                methodDef.getBlockText().setFont(new Font("Consolas", 20));
                methodDef.getBlockText().setFill(Color.GOLDENROD);
                generator.addCodeBlock(methodDef);

                startendDeclaration= new CodeBlock("start = 0, end = array.length - 1;", 15);
                generator.addCodeBlock(startendDeclaration);

                whileText = new CodeBlock("while(", 15);
                whileCondition = new CodeBlock("start <= end");
                closingParen = new CodeBlock(")");
                ArrayList<CodeBlock> whileLoopHeader = new ArrayList<>(Arrays.asList(whileText, whileCondition, closingParen));
                generator.addCodeBlocks(whileLoopHeader);

                divider = new CodeBlock("middle = floor((start + end) / 2);", 35);
                generator.addCodeBlock(divider);

                keyEquals = new CodeBlock("if(array[middle] == key)", 35);
                matchFound = new CodeBlock("return middle;", 55);
                generator.addCodeBlock(keyEquals);
                generator.addCodeBlock(matchFound);

                keyLess = new CodeBlock("else if(array[middle] > key)", 35);
                goLeft = new CodeBlock("end = middle - 1;", 55);
                generator.addCodeBlock(keyLess);
                generator.addCodeBlock(goLeft);

                elseText = new CodeBlock("else", 50);
                goRight = new CodeBlock("start = middle + 1;", 75);
                generator.addCodeBlock(elseText);
                generator.addCodeBlock(goRight);

                noMatch = new CodeBlock("return -1;", 15);
                generator.addCodeBlock(noMatch);

                VBox codetainer = new VBox();
                codetainer.setSpacing(2);
                codetainer.setBackground(new Background(new BackgroundFill(Color.INDIGO, null, null)));

                codetainer.getChildren().addAll(generator.getCodeBlocks());
                titledPane = new TitledPane("Code", codetainer);

                algoTracerContainer = new VBox();
                algoTracerContainer.setPrefWidth(pane.getLayoutBounds().getWidth()/2);
                final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;

                algoTracerContainer.setLayoutX(indentX);
                algoTracerContainer.setLayoutY((pane.getLayoutBounds().getCenterY()) - 50);

                algoTracerContainer.getChildren().add(titledPane);
                algoTracer.getChildren().add(algoTracerContainer);
            }
        }

        Visualiser binarySearchVis = new Visualiser();
        binarySearchVis.initAlgoTracer();

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);

        Button newSearchButton = new Button("New search");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);

        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer);
    }

    public Scene getScene() {
        return scene;
    }

}

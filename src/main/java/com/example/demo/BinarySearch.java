package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.RandomGenerator;
import com.example.demo.utils.Transitions;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BinarySearch {
    private Scene scene;

    public BinarySearch () {
        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        scene = new Scene(pane, 1366, 500, Color.GHOSTWHITE);
        final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
        mainArea.setLayoutX(20);
        mainArea.setLayoutY(20);
        class Visualiser {
            int index = -1;
            ArrayList<Comparable> inputArray;
            Comparable key;

            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList <FillTransition> codeAnims = new ArrayList<>();
            ArrayList <Transition> translateTransitions = new ArrayList<>();
            ArrayList<Transition> allAnimations = new ArrayList<>();
            CharBlock[] charBlocks;
            CharBlock keyBlock;
            Polygon startPointer, midPointer, endPointer;
            Text startPointerLabel, midPointerLabel, endPointerLabel;
            double startPointerWidth, midPointerWidth, endPointerWidth;

            Group mainAreaContainer;
            CodeBlock methodDef, startendDeclaration, whileText, whileCondition, closingParen, divider, keyEquals, matchFound, keyLess, goLeft, elseText, goRight, noMatch;
            VBox algoTracerContainer;
            TitledPane titledPane;

            void initMainArea() {
                inputArray = RandomGenerator.generateRandomNumbers(5, 15, 10);
                Collections.sort(inputArray);

                charBlocks = new CharBlock[inputArray.size()];
                key = RandomGenerator.generateRandomNumber(0, 15);
                mainAreaContainer = new Group();
                int blockIndent = 60;
                for (int c = 0; c < inputArray.size(); c++) {
                    CharBlock charBlock = new CharBlock((blockIndent + 15) * c, SCREENCENTER_Y + 70, 60, "" + inputArray.get(c), null, 40);
                    Rectangle rect = charBlock.getRect();
                    rect.setStroke(Color.BLACK);
                    rect.setArcWidth(5);
                    rect.setArcHeight(5);

                    Text indexText = new Text("" + c);
                    indexText.setFont(new Font("Consolas", 30));
                    indexText.setFill(Color.ROSYBROWN);
                    indexText.setX(rect.getLayoutBounds().getCenterX() - indexText.getLayoutBounds().getCenterX());
                    indexText.setY(rect.getLayoutBounds().getMaxY() + 30);

                    charBlocks[c] = charBlock;

                    mainAreaContainer.getChildren().addAll(charBlock.getBlock(),indexText);
                }

                int middleBlockIndex = (int) Math.floor(charBlocks.length / 2);
                keyBlock = new CharBlock(charBlocks[middleBlockIndex].getRect().getX(), SCREENCENTER_Y / 1.5, 60, "" + key, null, 40);
                keyBlock.getRect().setStroke(Color.BLACK);
                keyBlock.getRect().setArcWidth(5);
                keyBlock.getRect().setArcHeight(5);

                Text keyLabel = new Text(keyBlock.getBlockText().getLayoutBounds().getMinX() - 10, keyBlock.getRect().getLayoutBounds().getMinY() - 10, "key");
                keyLabel.setFill(Color.BLACK);
                keyLabel.setFont(new Font("Consolas", 30));

                startPointer = new Polygon();
                startPointer.getPoints().addAll(
                        charBlocks[0].getBlockText().getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[0].getBlockText().getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[0].getBlockText().getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                startPointerWidth = startPointer.getLayoutBounds().getWidth();
                startPointer.setFill(Color.BLUE);
                startPointerLabel = new Text(startPointer.getLayoutBounds().getMinX() - 10 , startPointer.getLayoutBounds().getMaxY() - 20, "start");
                startPointerLabel.setFont(new Font("Consolas", 15));
                startPointerLabel.setFill(Color.BLACK);

                midPointer = new Polygon();
                int midIndex = (charBlocks.length - 1)/2;
                midPointer.getPoints().addAll(
                        charBlocks[midIndex].getBlockText().getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[midIndex].getBlockText().getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[midIndex].getBlockText().getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                midPointerWidth = midPointer.getLayoutBounds().getWidth();
                midPointer.setFill(Color.RED);
                midPointerLabel = new Text(midPointer.getLayoutBounds().getMinX() - 10 , midPointer.getLayoutBounds().getMaxY() - 20, "middle");
                midPointerLabel.setFont(new Font("Consolas", 15));
                midPointerLabel.setFill(Color.BLACK);

                endPointer = new Polygon();
                int lastIndex = charBlocks.length - 1;
                endPointer.getPoints().addAll(
                        charBlocks[lastIndex].getBlockText().getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[lastIndex].getBlockText().getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        charBlocks[lastIndex].getBlockText().getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                endPointerWidth = endPointer.getLayoutBounds().getWidth();
                endPointer.setFill(Color.GREEN);
                endPointerLabel = new Text(endPointer.getLayoutBounds().getMinX() , endPointer.getLayoutBounds().getMaxY() - 20, "end");
                endPointerLabel.setFont(new Font("Consolas", 15));
                endPointerLabel.setFill(Color.BLACK);

                mainAreaContainer.getChildren().addAll(keyBlock.getBlock(), keyLabel, startPointer, startPointerLabel, midPointer, midPointerLabel, endPointer, endPointerLabel);
                mainArea.getChildren().add(mainAreaContainer);
            }
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

            int search() {
                int start = 0, end = inputArray.size() - 1, middle;

                FillTransition whileConditionAnim = Transitions.createHighlighter(whileCondition.getRect(), "code", null);
                codeAnims.add(whileConditionAnim);
                while (start <= end) {
                    FillTransition dividerAnim = Transitions.createHighlighter(divider.getRect(), "code", null);
                    codeAnims.add(dividerAnim);
                    middle = (start + end) / 2;

                    FillTransition keyEqualsAnim = Transitions.createHighlighter(keyEquals.getRect(), "code", null);
                    codeAnims.add(keyEqualsAnim);
                    if(inputArray.get(middle) == key) {
                        codeAnims.add(Transitions.createHighlighter(matchFound.getRect(), "code", null));
                        this.setIndex(middle);
                        return middle;
                    } else if (inputArray.get(middle).compareTo(key) > 0) {
                        codeAnims.add(Transitions.createHighlighter(keyLess.getRect(), "code", null));
                        codeAnims.add(Transitions.createHighlighter(goLeft.getRect(), "code", null));
                        end = middle - 1;
                    } else {
                        codeAnims.add(Transitions.createHighlighter(goRight.getRect(), "code", null));
                        start = middle + 1;
                    }
                }
                codeAnims.add(Transitions.createHighlighter(noMatch.getRect(), "code", null));
                return -1;
            }

            public void handleOnFinished() {
                for(int count = 0; count < codeAnims.size() - 1; count++) {
                    FillTransition currentAnim = codeAnims.get(count);
                    if(currentAnim.getOnFinished() != null) {
                        continue;
                    }
                    FillTransition nextAnim = codeAnims.get(count + 1);
                    currentAnim.setOnFinished(e -> nextAnim.play());
                }
            }

            void start() {
                codeAnims.get(0).play();
            }
            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }

        Visualiser binarySearchVis = new Visualiser();
        binarySearchVis.initAlgoTracer();
        binarySearchVis.initMainArea();
        binarySearchVis.search();
        binarySearchVis.handleOnFinished();
        binarySearchVis.start();

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

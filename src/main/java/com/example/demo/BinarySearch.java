package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.RandomGenerator;
import com.example.demo.utils.Transitions;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
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
import java.util.Objects;

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
            ArrayList <TranslateTransition> translateTransitions = new ArrayList<>();

            ArrayList<TranslateTransition> moveMiddleTransitions = new ArrayList<>();
            ArrayList<FadeTransition> fadeTransitions = new ArrayList<>();
            ArrayList<Transition> allAnimations = new ArrayList<>();

            ArrayList<Text> indexTexts = new ArrayList<>();
            ArrayList<Double> indexTextCentres = new ArrayList<>();
            CharBlock[] charBlocks;
            CharBlock keyBlock;
            Group startPointer, midPointer, endPointer;
            Polygon startPointerShape, midPointerShape, endPointerShape;
            Text startPointerLabel, midPointerLabel, endPointerLabel;
            double pointerWidth, pointerHeight;

            Group mainAreaContainer;
            CodeBlock methodDef, startendDeclaration, whileText, whileCondition, closingParen, divider, keyEquals, matchFound, keyLess, goLeft, elseText, goRight, noMatch;
            VBox algoTracerContainer;
            TitledPane titledPane;

            void initMainArea() {
                inputArray = RandomGenerator.generateRandomNumbers(5, 15, 10);
                Collections.sort(inputArray);

                charBlocks = new CharBlock[inputArray.size()];
                key = RandomGenerator.generateRandomNumber(5, 10);
                mainAreaContainer = new Group();
                for (int c = 0; c < inputArray.size(); c++) {
                    CharBlock charBlock;
                    if(c == 0) {
                        charBlock = new CharBlock(20, SCREENCENTER_Y + 70, 60, "" + inputArray.get(c), null, 40);
                    } else {
                        double previousBlockEdge = charBlocks[c-1].getRect().getLayoutBounds().getMaxX();
                        charBlock = new CharBlock(previousBlockEdge + 15, SCREENCENTER_Y + 70, 60, "" + inputArray.get(c), null, 40);
                    }
                    Rectangle rect = charBlock.getRect();
                    rect.setStroke(Color.BLACK);
                    rect.setArcWidth(5);
                    rect.setArcHeight(5);

                    Text indexText = new Text("" + c);
                    indexText.setFont(new Font("Consolas", 30));
                    indexText.setFill(Color.ROSYBROWN);
                    indexText.setX(rect.getLayoutBounds().getCenterX() - indexText.getLayoutBounds().getCenterX());
                    indexText.setY(rect.getLayoutBounds().getMaxY() + 30);
                    indexTexts.add(indexText);

                    charBlocks[c] = charBlock;

                    mainAreaContainer.getChildren().addAll(charBlock.getBlock(),indexText);
                }

                for(Text indexText: indexTexts) {
                    Bounds bounds = indexText.localToScene(indexText.getBoundsInLocal());
                    indexTextCentres.add(bounds.getCenterX());
                }

                int middleBlockIndex = (int) Math.floor(charBlocks.length / 2);
                keyBlock = new CharBlock(charBlocks[middleBlockIndex].getRect().getX(), SCREENCENTER_Y / 1.5, 60, "" + key, null, 40);
                keyBlock.getRect().setStroke(Color.BLACK);
                keyBlock.getRect().setArcWidth(5);
                keyBlock.getRect().setArcHeight(5);

                Text keyLabel = new Text(keyBlock.getBlockText().getLayoutBounds().getMinX() - 10, keyBlock.getRect().getLayoutBounds().getMinY() - 10, "key");
                keyLabel.setFill(Color.BLACK);
                keyLabel.setFont(new Font("Consolas", 30));

                startPointerShape = new Polygon();
                startPointerShape.getPoints().addAll(
                        indexTexts.get(0).getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(0).getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(0).getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                pointerWidth = startPointerShape.getLayoutBounds().getWidth();
                startPointerShape.setFill(Color.BLUE);
                startPointerLabel = new Text(startPointerShape.getLayoutBounds().getMinX() - 10 , startPointerShape.getLayoutBounds().getMaxY() - 20, "start");
                startPointerLabel.setFont(new Font("Consolas", 15));
                startPointerLabel.setFill(Color.BLACK);
                startPointer = new Group();
                startPointer.getChildren().addAll(startPointerShape, startPointerLabel);

                midPointerShape = new Polygon();
                int midIndex = (charBlocks.length - 1)/2;
                midPointerShape.getPoints().addAll(
                        indexTexts.get(midIndex).getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(midIndex).getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(midIndex).getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                midPointerShape.setFill(Color.RED);
                midPointerLabel = new Text(midPointerShape.getLayoutBounds().getMinX() - 10 , midPointerShape.getLayoutBounds().getMaxY() - 20, "middle");
                midPointerLabel.setFont(new Font("Consolas", 15));
                midPointerLabel.setFill(Color.BLACK);
                midPointer = new Group();
                midPointer.getChildren().addAll(midPointerShape, midPointerLabel);
                midPointer.setOpacity(0);
                midPointer.setId("midPointer");

                endPointerShape = new Polygon();
                int lastIndex = charBlocks.length - 1;
                endPointerShape.getPoints().addAll(
                        indexTexts.get(lastIndex).getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(lastIndex).getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(lastIndex).getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                endPointerShape.setFill(Color.GREEN);
                endPointerLabel = new Text(endPointerShape.getLayoutBounds().getMinX() - 5 , endPointerShape.getLayoutBounds().getMaxY() - 20, "end");
                endPointerLabel.setFont(new Font("Consolas", 15));
                endPointerLabel.setFill(Color.BLACK);
                endPointer = new Group();
                endPointer.getChildren().addAll(endPointerShape, endPointerLabel);

                pointerWidth = endPointerShape.getLayoutBounds().getWidth();
                pointerHeight = endPointer.getLayoutBounds().getHeight();

                mainAreaContainer.getChildren().addAll(keyBlock.getBlock(), keyLabel, startPointer, midPointer, endPointer);
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
                whileCondition.getRect().setId("whileBlock");
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
                int start = 0, end = inputArray.size() - 1, middle = (start + end) / 2;

                FillTransition whileConditionAnim = Transitions.createHighlighter(whileCondition.getRect(), "code", null);
                codeAnims.add(whileConditionAnim);

                FadeTransition fadeInMidPointer = Transitions.fadeItemIn(midPointer);
                // we're multiplying by 2 because the CodeBlock animations have a cycle count of 2. In other words, they take twice the time of their specified duration to complete
                fadeInMidPointer.setDelay(whileConditionAnim.getDuration().multiply(2));
                fadeTransitions.add(fadeInMidPointer);

                while (start <= end) {
                    FillTransition dividerAnim = Transitions.createHighlighter(divider.getRect(), "code", null);
                    codeAnims.add(dividerAnim);

                    int oldMiddle = middle;
                    middle = (start + end) / 2;
                    double distanceFromNewMiddle = indexTextCentres.get(middle) - indexTextCentres.get(oldMiddle);

                    if(start != 0 || end != inputArray.size() - 1) {
                        double yTranslation = 0;
                        if(middle == end || middle == start) {
                            yTranslation = -pointerHeight;
                        }
                        TranslateTransition moveMiddle = Transitions.translateX(midPointer, distanceFromNewMiddle,300);
                        moveMiddle.setByY(yTranslation);
                        translateTransitions.add(moveMiddle);
                        moveMiddleTransitions.add(moveMiddle);
                    }

                    FillTransition keyEqualsAnim = Transitions.createHighlighter(keyEquals.getRect(), "code", null);
                    codeAnims.add(keyEqualsAnim);

                    FillTransition checkBlockAnim = Transitions.createHighlighter(charBlocks[middle].getRect(), "char", null);
                    charBlockAnims.add(checkBlockAnim);

                    FillTransition keyBlockCheckAnim = Transitions.createHighlighter(keyBlock.getRect(), "char", null);

                    dividerAnim.setOnFinished(e -> {
                        keyEqualsAnim.play();
                        keyBlockCheckAnim.play();
                        checkBlockAnim.play();
                    });

                    if(inputArray.get(middle) == key) {
                        keyBlockCheckAnim.setToValue(Color.LAWNGREEN);
                        keyBlockCheckAnim.setCycleCount(1);

                        checkBlockAnim.setToValue(Color.LAWNGREEN);
                        checkBlockAnim.setCycleCount(1);

                        codeAnims.add(Transitions.createHighlighter(matchFound.getRect(), "code", null));
                        this.setIndex(middle);
                        return middle;
                    } else {
                        if (inputArray.get(middle).compareTo(key) > 0) {
                            double distance;
                            double yTranslation = 0;
                            if(end == start) {
                                yTranslation = -(pointerHeight);
                            }

                            if (middle - 1 < 0) {
                                // negating it so the translate X can move left
                                distance = -(pointerWidth * 3);
                                checkBlockAnim.setToValue(Color.RED);
                                checkBlockAnim.setCycleCount(1);

                                keyBlockCheckAnim.setToValue(Color.RED);
                                keyBlockCheckAnim.setCycleCount(1);
                            }
                            else {
                                distance = indexTextCentres.get(middle - 1) - indexTextCentres.get(end);
                            }

                            TranslateTransition goLeftAnim = Transitions.translateX(endPointer, distance, 200);
                            goLeftAnim.setByY(yTranslation);

                            translateTransitions.add(goLeftAnim);
                            checkBlockAnim.setOnFinished(e -> {
                                goLeftAnim.play();
                            });

                            codeAnims.add(Transitions.createHighlighter(keyLess.getRect(), "code", null));
                            codeAnims.add(Transitions.createHighlighter(goLeft.getRect(), "code", null));
                            end = middle - 1;
                        } else {
                            double distance;
                            double yTranslation = 0;
                            if(start + 1 == end) {
                                yTranslation = -(pointerHeight);
                            }

                            if (middle + 1 == inputArray.size()) {
                                distance = (pointerWidth * 3);
                                checkBlockAnim.setToValue(Color.RED);
                                checkBlockAnim.setCycleCount(1);

                                keyBlockCheckAnim.setToValue(Color.RED);
                                keyBlockCheckAnim.setCycleCount(1);
                            } else {
                                distance = indexTextCentres.get(middle + 1) - indexTextCentres.get(start);
                            }

                            TranslateTransition goRightAnim = Transitions.translateX(startPointer, distance, 200);
                            goRightAnim.setByY(yTranslation);
                            translateTransitions.add(goRightAnim);
                            checkBlockAnim.setOnFinished(e -> {
                                goRightAnim.play();
                            });

                            codeAnims.add(Transitions.createHighlighter(goRight.getRect(), "code", null));
                            start = middle + 1;
                        }
                    }
                    FadeTransition fadeOutMidPointer = Transitions.fadeItemOut(midPointer);
                    fadeTransitions.add(fadeOutMidPointer);
                    codeAnims.add(Transitions.createHighlighter(whileCondition.getRect(), "code", null));
                }

                codeAnims.add(Transitions.createHighlighter(noMatch.getRect(), "code", null));
                return -1;
            }

            public void handleOnFinished() {
                int moveMiddleSize = moveMiddleTransitions.size();
                int moveMiddleArrayCounter = 0;
                for(int count = 0; count < codeAnims.size() - 1; count++) {
                    FillTransition currentAnim = codeAnims.get(count);
                    FillTransition nextAnim = codeAnims.get(count + 1);
                    if(currentAnim.getOnFinished() != null) {
                        continue;
                    }
                    // handling an edge case where adding moveMiddle doesn't work from the search code
                    if(Objects.equals(currentAnim.getShape().getId(), "whileBlock")) {
                        int finalMoveMiddleArrayCounter = moveMiddleArrayCounter;
                        // We're doing this check so we don't set the first moveMiddle animation to play after the first while codeblock animation.
                        // The first while codeblock animation is supposed to play the midPointer fade-in animation
                        if((moveMiddleSize != 0) && (moveMiddleArrayCounter != moveMiddleSize) && (count != 0)) {
                            FillTransition nextCodeAnim = codeAnims.get(count + 1);
                            currentAnim.setOnFinished(e -> {
                                moveMiddleTransitions.get(finalMoveMiddleArrayCounter).play();
                                nextCodeAnim.play();
                            });
                            moveMiddleArrayCounter++;
                        } else {
                            // handle any whileConditionAnim that should not have a moveMiddle anim after it
                            // check if you can modify the onFinished using getOnFinished
                            currentAnim.setOnFinished(e -> {
                                nextAnim.play();
                            });
                        }
                    } else {
                        // handle any other codeblock animations
                        // repeating for semantic
                        currentAnim.setOnFinished(e -> nextAnim.play());
                    }
                }
            }

            void start() {
                codeAnims.get(0).play();
                fadeTransitions.get(0).play();
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

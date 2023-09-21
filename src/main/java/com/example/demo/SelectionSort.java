package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.components.PlayingQueue;
import com.example.demo.components.SpeedSlider;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.DeepCopy;
import com.example.demo.utils.RandomGenerator;
import com.example.demo.utils.Transitions;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Queue;

public class SelectionSort {
    private Scene scene;

    public SelectionSort() {
        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        scene = new Scene(pane, 1366, 500, Color.GHOSTWHITE);
        // Stage stage = new Stage();
        final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
        mainArea.setLayoutX(20);
        HBox controls = new HBox();

        class Visualise {
            Slider speedSlider;
            boolean resetFlag = false;
            ArrayList<Comparable> inputArray = new ArrayList<>();

            ArrayList<Comparable> stickyInputArray = new ArrayList<>();
            CharBlock[] charBlocks;
            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList<Text> indexTexts = new ArrayList<>();
            ArrayList<Double> indexTextCentres = new ArrayList<>();

            ArrayList<Text> charBlockTexts = new ArrayList<>();
            ArrayList<Double> charBlockTextCentres = new ArrayList<>();
            ArrayList <FillTransition> codeAnims = new ArrayList<>();
            ArrayList <TranslateTransition> translateTransitions = new ArrayList<>();

            ArrayList<FadeTransition> fadeTransitions = new ArrayList<>();
            ArrayList<Transition> allAnimations = new ArrayList<>();
            Queue<Transition> playingQueue = new PlayingQueue().getPlayingQueue();
            Polygon elemPointer;
            Group minPointer;
            double pointerWidth;

            CodeBlock methodDef, tempDeclaration, outerForText, outerForInit, outerForComp, outerForIncrement, closingParen1, initialMinPos, innerForText, innerForInit, innerForComp, innerForIncrement, closingParen2, compareMin, newTempMin, setTemp, repositionOldMin, setNewMin;
            VBox algoTracerContainer;
            Group mainAreaContainer;
            TitledPane titledPane;
            final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
            final int indentY = (int) (pane.getLayoutBounds().getCenterY()) - 50;

            void initMainArea() {
                if(!resetFlag) {
                    inputArray = RandomGenerator.generateRandomNumbers(5, 30, 5);
                    // copy inputArray to stickyInputArray
                    DeepCopy.copy(inputArray, stickyInputArray);
                } else {
                    // copy stickyInputArray to inputArray
                    DeepCopy.copy(stickyInputArray, inputArray);
                }
//                inputArray = !resetFlag ? RandomGenerator.generateRandomNumbers(5, 30, 5) : stickyInputArray;
//                stickyInputArray = !resetFlag ? inputArray : stickyInputArray;

                charBlocks = new CharBlock[inputArray.size()];
                mainAreaContainer = new Group();

                for (int c = 0; c < inputArray.size(); c++) {
                    CharBlock charBlock;
                    if(c == 0) {
                        charBlock = new CharBlock(20, SCREENCENTER_Y + 70, 60, String.valueOf(inputArray.get(c)), null, 40);
                    } else {
                        double previousBlockEdge = charBlocks[c-1].getRect().getLayoutBounds().getMaxX();
                        charBlock = new CharBlock(previousBlockEdge + 15, SCREENCENTER_Y + 70, 60, String.valueOf(inputArray.get(c)), null, 40);
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

                    charBlockTextCentres.add(charBlock.getBlockText().getLayoutBounds().getCenterX());

                    charBlocks[c] = charBlock;

                    mainAreaContainer.getChildren().addAll(charBlock.getBlock(),indexText);

                }

                for(Text indexText: indexTexts) {
                    Bounds bounds = indexText.localToScene(indexText.getBoundsInLocal());
                    indexTextCentres.add(bounds.getCenterX());
                }

                Polygon minPointerShape = new Polygon();
                minPointerShape.getPoints().addAll(
                        indexTexts.get(0).getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(0).getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(0).getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );

                pointerWidth = minPointerShape.getLayoutBounds().getWidth();
                minPointerShape.setFill(Color.BLUE);
                Text minPointerLabel = new Text(minPointerShape.getLayoutBounds().getMinX() - 5, minPointerShape.getLayoutBounds().getMaxY() - 20, "min");
                minPointerLabel.setFont(new Font("Consolas", 15));
                minPointerLabel.setFill(Color.BLACK);

                minPointer = new Group(minPointerShape, minPointerLabel);

                elemPointer = new Polygon();
                elemPointer.getPoints().addAll(
                        indexTexts.get(1).getLayoutBounds().getMinX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(1).getLayoutBounds().getMaxX(), charBlocks[0].getRect().getY() - 20,
                        indexTexts.get(1).getLayoutBounds().getCenterX(), charBlocks[0].getRect().getY() - 5
                );
                elemPointer.setFill(Color.RED);

                mainAreaContainer.getChildren().addAll(minPointer, elemPointer);
                mainArea.getChildren().add(mainAreaContainer);
            }
            void initAlgoTracer() {
                CodeBlockGenerator generator = new CodeBlockGenerator();

                methodDef = new CodeBlock( "selectionSort(array)", 5);
                methodDef.getBlockText().setFont(new Font("Consolas", 20));
                methodDef.getBlockText().setFill(Color.GOLDENROD);
                generator.addCodeBlock(methodDef);

                tempDeclaration = new CodeBlock("temp;", 25);
                generator.addCodeBlock(tempDeclaration);

                outerForText = new CodeBlock("for(", 25);
                outerForInit = new CodeBlock("i = 0; ");
                outerForComp = new CodeBlock("i < array.length - 1; ");
                outerForIncrement = new CodeBlock("i++");
                closingParen1 = new CodeBlock(")");
                ArrayList<CodeBlock> forLoopHeader1 = new ArrayList<>(Arrays.asList(outerForText, outerForInit, outerForComp, outerForIncrement, closingParen1));
                generator.addCodeBlocks(forLoopHeader1);

                initialMinPos = new CodeBlock("min = i;", 40);
                generator.addCodeBlock(initialMinPos);

                innerForText = new CodeBlock("for(", 40);
                innerForInit = new CodeBlock("j = i + 1; ");
                innerForComp = new CodeBlock("j < array.length; ");
                innerForIncrement = new CodeBlock("j++");
                closingParen2 = new CodeBlock(")");
                ArrayList<CodeBlock> forLoopHeader2 = new ArrayList<>(Arrays.asList(innerForText, innerForInit, innerForComp, innerForIncrement, closingParen2));
                generator.addCodeBlocks(forLoopHeader2);

                compareMin = new CodeBlock("if(array[j] < array[min])", 55);
                newTempMin = new CodeBlock("min = j;", 65);
                generator.addCodeBlock(compareMin);
                generator.addCodeBlock(newTempMin);

                setTemp = new CodeBlock("temp = arr;", 40);
                repositionOldMin = new CodeBlock("array[min] = array[i];", 40);
                setNewMin = new CodeBlock("array[i] = temp;", 40);
                generator.addCodeBlock(setTemp);
                generator.addCodeBlock(repositionOldMin);
                generator.addCodeBlock(setNewMin);

                VBox codetainer = new VBox();
                codetainer.setBackground(new Background(new BackgroundFill(Color.INDIGO, null, null)));

                codetainer.getChildren().addAll(generator.getCodeBlocks());
                titledPane = new TitledPane("Code", codetainer);

                algoTracerContainer = new VBox();
                algoTracerContainer.setPrefWidth(pane.getLayoutBounds().getWidth()/2);
                algoTracerContainer.setLayoutX(indentX - 50);
                algoTracerContainer.setLayoutY(indentY);

                algoTracerContainer.getChildren().add(titledPane);
                algoTracer.getChildren().add(algoTracerContainer);
            }

            void sort() {
                int min = 0;
                Comparable temp;
                final double distanceBetweenAdjBlocks = indexTextCentres.get(1) - indexTextCentres.get(0);
                FillTransition outerForCompAnim = Transitions.createHighlighter(outerForComp.getRect(), "code", null);
                codeAnims.add(outerForCompAnim);
                for(int i = 0; i < inputArray.size() - 1; i++){
                    int oldMin = min;
                    min = i;

                    FillTransition initialMinPosAnim = Transitions.createHighlighter(initialMinPos.getRect(), "code", null);
                    codeAnims.add(initialMinPosAnim);

                    // moveMinPointer to the next block
                    TranslateTransition repositionMinPointer = null;
                    TranslateTransition repositionElemPointer;
                    // we want to move the min pointer at the start of each iteration AFTER the first iteration
                    if(i > 0) {
                        // bring the min pointer back from the position it had on the last iteration and place it in the new correct position
                        double displacementFromNewMin = indexTextCentres.get(min) - indexTextCentres.get(oldMin);
                        repositionMinPointer = Transitions.translateX(minPointer, displacementFromNewMin, null);
                        translateTransitions.add(repositionMinPointer);

                        double displacementFromNewPos = indexTextCentres.get(i + 1) - (indexTextCentres.get(indexTextCentres.size() - 1) + distanceBetweenAdjBlocks);
                        repositionElemPointer = Transitions.translateX(elemPointer, displacementFromNewPos, null);
                        translateTransitions.add(repositionElemPointer);

                        repositionMinPointer.setOnFinished(e -> {
                            playingQueue.poll();
                            repositionElemPointer.play();
                            playingQueue.add(repositionElemPointer);
                        });
                    }

                    TranslateTransition finalRepositionMinPointer = repositionMinPointer;
                    outerForCompAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        initialMinPosAnim.play();
                        playingQueue.add(initialMinPosAnim);
                        if (finalRepositionMinPointer != null) {
                            finalRepositionMinPointer.play();
                            playingQueue.add(finalRepositionMinPointer);
                        }
                    });

                    codeAnims.add(Transitions.createHighlighter(innerForInit.getRect(), "code", null));
                    FillTransition innerForCompAnim = Transitions.createHighlighter(innerForComp.getRect(), "code", null);
                    codeAnims.add(innerForCompAnim);
                    for(int j = i + 1; j < inputArray.size(); j++){
                        FillTransition compareMinAnim = Transitions.createHighlighter(compareMin.getRect(), "code", null);
                        codeAnims.add(compareMinAnim);
                        FillTransition checkBlockAnim = Transitions.createHighlighter(charBlocks[j].getRect(), "char", null);
                        charBlockAnims.add(checkBlockAnim);
                        if(inputArray.get(j).compareTo(inputArray.get(min)) < 0) {
                            FillTransition newTempMinAnim = Transitions.createHighlighter(newTempMin.getRect(), "code", null);
                            codeAnims.add(newTempMinAnim);
                            double distanceFromOldMin = indexTextCentres.get(j) - indexTextCentres.get(min);
                            min = j;


                            checkBlockAnim.setToValue(Color.LAWNGREEN);

                            TranslateTransition moveMinPointerForward = Transitions.translateX(minPointer, distanceFromOldMin, null);
                            translateTransitions.add(moveMinPointerForward);

                            compareMinAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                newTempMinAnim.play();
                                playingQueue.add(newTempMinAnim);
                                moveMinPointerForward.play();
                                playingQueue.add(moveMinPointerForward);
                            });
                        }

                        innerForCompAnim.setOnFinished(e -> {
                            playingQueue.poll();
                            compareMinAnim.play();
                            playingQueue.add(compareMinAnim);
                            checkBlockAnim.play();
                            playingQueue.add(checkBlockAnim);
                        });

                        FillTransition innerForIncrementAnim = Transitions.createHighlighter(innerForIncrement.getRect(), "code", null);
                        codeAnims.add(innerForIncrementAnim);

                        TranslateTransition moveElemPointerForward = Transitions.translateX(elemPointer, distanceBetweenAdjBlocks, null);
                        translateTransitions.add(moveElemPointerForward);

                        innerForCompAnim = Transitions.createHighlighter(innerForComp.getRect(), "code", null);
                        codeAnims.add(innerForCompAnim);

                        FillTransition finalInnerForCompAnim = innerForCompAnim;
                        innerForIncrementAnim.setOnFinished(e -> {
                            playingQueue.poll();
                            moveElemPointerForward.play();
                            playingQueue.add(moveElemPointerForward);
                            finalInnerForCompAnim.play();
                            playingQueue.add(finalInnerForCompAnim);
                        });
                    }
                    // hold new minimum value
                    FillTransition setTempAnim = Transitions.createHighlighter(setTemp.getRect(), "code", null);
                    codeAnims.add(setTempAnim);
                    temp = inputArray.get(min);

                    // Swap animations - fade numbers
                    FadeTransition fadeOldMinOut = Transitions.fadeItemOut(charBlocks[inputArray.indexOf(temp)].getBlockText(), 100);
                    fadeTransitions.add(fadeOldMinOut);
                    FadeTransition fadeOldMinIn = Transitions.fadeItemIn(charBlocks[inputArray.indexOf(temp)].getBlockText(), 100);
                    fadeTransitions.add(fadeOldMinIn);
                    fadeOldMinOut.setOnFinished( e -> {
                        playingQueue.poll();
                        fadeOldMinIn.play();
                        playingQueue.add(fadeOldMinIn);
                    });

                    // Swap animations - fade numbers
                    FadeTransition fadeNewMinOut = Transitions.fadeItemOut(charBlocks[i].getBlockText(), 100);
                    fadeTransitions.add(fadeNewMinOut);
                    FadeTransition fadeNewMinIn = Transitions.fadeItemIn(charBlocks[i].getBlockText(), 100);
                    fadeTransitions.add(fadeNewMinIn);
                    fadeNewMinOut.setOnFinished(e -> {
                        playingQueue.poll();
                        fadeNewMinIn.play();
                        playingQueue.add(fadeNewMinIn);
                    });

                    // change the value on screen
                    Comparable finalTemp = temp;
                    int finalI = i;
                    String finalCharBlockTemp = temp.toString();

                    // set old minimum to its new position
                    FillTransition repositionOldMinAnim = Transitions.createHighlighter(repositionOldMin.getRect(), "code", null);
                    codeAnims.add(repositionOldMinAnim);
                    inputArray.set(inputArray.indexOf(temp), inputArray.get(i));

                    // set new minimum
                    FillTransition setNewMinAnim = Transitions.createHighlighter(setNewMin.getRect(), "code", null);
                    codeAnims.add(setNewMinAnim);
                    inputArray.set(i, temp);

                    repositionOldMinAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        setNewMinAnim.play();
                        playingQueue.add(setNewMinAnim);
                        fadeOldMinOut.play();
                        // loop through charBlocks and check which has the same value as temp, then change it
                        for(int counter = 0; counter < charBlocks.length; counter++) {
                            if(charBlocks[counter].getBlockText().getText().equals(finalCharBlockTemp)) {
                                charBlocks[counter].setBlockText(charBlocks[finalI].getBlockText().getText());
                            }
                        }

                        playingQueue.add(fadeOldMinOut);
                        fadeNewMinOut.play();

                        charBlocks[finalI].setBlockText(finalTemp.toString());
                        playingQueue.add(fadeNewMinOut);
                    });

                    FillTransition outerForIncrementAnim = Transitions.createHighlighter(outerForIncrement.getRect(), "code", null);
                    codeAnims.add(outerForIncrementAnim);

                    outerForCompAnim = Transitions.createHighlighter(outerForComp.getRect(), "code", null);
                    codeAnims.add(outerForCompAnim);

                    FillTransition finalOuterForCompAnim = outerForCompAnim;
                    outerForIncrementAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        // Colour the sorted blocks
                        if(finalI + 1 != charBlocks.length - 1) {
                            charBlocks[finalI].getRect().setFill(Color.LAWNGREEN);
                        } else {
                            charBlocks[finalI].getRect().setFill(Color.LAWNGREEN);
                            charBlocks[finalI + 1].getRect().setFill(Color.LAWNGREEN);
                        }

                        if(finalI + 1 == inputArray.size() - 1) {
                            FadeTransition fadeOutMinPointer = Transitions.fadeItemOut(minPointer, null);
                            fadeTransitions.add(fadeOutMinPointer);
                            FadeTransition fadeOutElemPointer = Transitions.fadeItemOut(elemPointer, null);
                            fadeTransitions.add(fadeOutElemPointer);

                            fadeOutMinPointer.play();
                            playingQueue.add(fadeOutMinPointer);
                            fadeOutElemPointer.play();
                            playingQueue.add(fadeOutElemPointer);
                        }

                        finalOuterForCompAnim.play();
                        playingQueue.add(finalOuterForCompAnim);
                    });
                }

            }

            public void handleOnFinished() {
                // handle codeblock anims
                for(int count = 0; count < codeAnims.size() - 1; count++) {
                    FillTransition currentAnim = codeAnims.get(count);
                    if(currentAnim.getOnFinished() != null) {
                        continue;
                    }
                    FillTransition nextAnim = codeAnims.get(count + 1);
                    currentAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        nextAnim.play();
                        playingQueue.add(nextAnim);
                    });
                }

                // Handle char block anims removal from playing queue
                for(FillTransition ft: charBlockAnims) {
                    if(ft.getOnFinished() == null) {
                        ft.setOnFinished(e -> playingQueue.poll());
                    }
                }
            }

            public void setAllAnimations() {
                allAnimations.addAll(translateTransitions);
                allAnimations.addAll(codeAnims);
                allAnimations.addAll(charBlockAnims);
                allAnimations.addAll(fadeTransitions);
            }

            private void setAllAnimationsSpeed() {
                double speedFactor = speedSlider.valueProperty().doubleValue();
                for (Transition animation : allAnimations) {
                    animation.setRate(speedFactor);
                }
            }

            public void initControls() {
                speedSlider = new SpeedSlider().getSpeedSlider();
                speedSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                    double speedFactor = Math.abs(newValue.doubleValue());

                    for (Transition animation : allAnimations) {
                        animation.setRate(speedFactor);
                    }
                });

                controls.getChildren().add(speedSlider);
            }

            private void clear() {
                algoTracerContainer.getChildren().remove(titledPane);
                mainArea.getChildren().remove(mainAreaContainer);

                for(Transition transition: playingQueue) {
                    if(transition.getStatus().equals(Animation.Status.RUNNING)) {
                        transition.stop();
                    }
                }

                translateTransitions.clear();
                codeAnims.clear();
                charBlockAnims.clear();
                fadeTransitions.clear();

                // reset all Char block colours
                for (CharBlock charBlock : charBlocks) {
                    charBlock.getRect().setFill(Color.ORANGE);
                }
            }
            public void generateNew() {
                // generate new chars and change the codeblock text value
                clear();

                this.resetFlag = false;
                this.initMainArea();
                this.initAlgoTracer();
                this.sort();
                this.handleOnFinished();
                this.setAllAnimations();
                this.setAllAnimationsSpeed();
                this.start();
            }

            void reset() {
                clear();

                this.resetFlag = true;
                this.initMainArea();
                this.initAlgoTracer();
                this.sort();
                this.handleOnFinished();
                this.setAllAnimations();
                this.setAllAnimationsSpeed();
                this.start();
            }

            public void start() {
                codeAnims.get(0).play();
            }
        }

        Visualise selectionSortVis = new Visualise();
        selectionSortVis.initAlgoTracer();
        selectionSortVis.initMainArea();
        selectionSortVis.sort();
        selectionSortVis.handleOnFinished();
        selectionSortVis.setAllAnimations();
        selectionSortVis.initControls();
        selectionSortVis.start();

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);
        restartButton.setOnMouseClicked(e -> selectionSortVis.reset());

        Button newSearchButton = new Button("New sort");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);
        newSearchButton.setOnMouseClicked(e -> selectionSortVis.generateNew());

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);
        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);

        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);
    }

    public Scene getScene() {
        return scene;
    }
}

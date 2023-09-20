package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.components.PlayingQueue;
import com.example.demo.components.SpeedSlider;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.RandomGenerator;
import com.example.demo.utils.Transitions;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
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
import java.util.Queue;


public class LinearSearch {
    private Scene scene;
    /*@Override
    public void start(Stage primaryStage) throws IOException {*/
    public LinearSearch() {
        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        scene = new Scene(pane, 1366, 500, Color.GHOSTWHITE);
        // Stage stage = new Stage();
        final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
        mainArea.setLayoutX(20);

        Font font = new Font("Consolas", 20);

        HBox controls = new HBox();

        class Visualise {
            int index = -1;
            Slider speedSlider;
            boolean resetFlag = false;
            ArrayList<Comparable> inputArray = new ArrayList<>();
            Comparable key = null;
            CharBlock[] charBlocks;
            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList<Text> indexTexts = new ArrayList<>();
            ArrayList<Double> indexTextCentres = new ArrayList<>();
            ArrayList <FillTransition> codeAnims = new ArrayList<>();
            ArrayList <TranslateTransition> translateTransitions = new ArrayList<>();
            ArrayList<Transition> allAnimations = new ArrayList<>();
            Queue<Transition> playingQueue = new PlayingQueue().getPlayingQueue();
            CharBlock keyBlock;
            Polygon pointer;
            double pointerWidth;

            CodeBlock methodDef, indexText, fortext, init, counterComp, increment, closingParen, keyComp, matchFound, breakText, returnText;

            VBox algoTracerContainer;
            Group mainAreaContainer;
            TitledPane titledPane;
            final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
            final int indentY = (int) (pane.getLayoutBounds().getCenterY()) - 50;

            void initMainArea() {
                inputArray = inputArray.isEmpty() || !resetFlag ? RandomGenerator.generateRandomCharacters( 10) : inputArray;
                charBlocks = new CharBlock[inputArray.size()];
                key = key == null || !resetFlag ? RandomGenerator.generateRandomCharacter() : key;
                mainAreaContainer = new Group();
                int blockIndent = 60;

                for (int c = 0; c < inputArray.size(); c++) {
                    CharBlock charBlock = new CharBlock((blockIndent + 15) * c, SCREENCENTER_Y, 60, "" + inputArray.get(c), null, 40);
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

                pointer = new Polygon();
                /*pointer.getPoints().addAll(
                        0.0, 10.0, // first vertex
                        20.0, 10.0,// second vertex
                        10.0, 20.0 // third vertex
                );*/
                pointer.getPoints().addAll(
                        charBlocks[0].getBlockText().getLayoutBounds().getMinX(), SCREENCENTER_Y - 20.0,
                        charBlocks[0].getBlockText().getLayoutBounds().getMaxX(), SCREENCENTER_Y - 20.0,
                        charBlocks[0].getBlockText().getLayoutBounds().getCenterX(), SCREENCENTER_Y - 10.0
                );

                pointerWidth = pointer.getLayoutBounds().getWidth();
                pointer.setFill(Color.BLUE);

                mainAreaContainer.getChildren().addAll(keyBlock.getBlock(), keyLabel, pointer);
                mainArea.getChildren().add(mainAreaContainer);
            }

            void initAlgoTracer() {
                // ------------------- ALGO TRACER --------------------------------
                CodeBlockGenerator generator = new CodeBlockGenerator();

                methodDef = new CodeBlock( "linearSearch(key, arr)", 5);
                methodDef.getBlockText().setFont(new Font("Consolas", 20));
                methodDef.getBlockText().setFill(Color.GOLDENROD);
                generator.addCodeBlock(methodDef);

                indexText = new CodeBlock("index = -1;", 15);
                generator.addCodeBlock(indexText);

                fortext = new CodeBlock("for(", 25);
                init = new CodeBlock("i = 0;");
                counterComp = new CodeBlock("i < arr.length;");
                increment = new CodeBlock("i++");
                closingParen = new CodeBlock(")");
                ArrayList<CodeBlock> forLoopHeader1 = new ArrayList<>(Arrays.asList(fortext, init, counterComp, increment, closingParen));
                generator.addCodeBlocks(forLoopHeader1);

                keyComp = new CodeBlock("if(arr[i] == key)", 35);
                generator.addCodeBlock(keyComp);
                matchFound = new CodeBlock("index = i;", 45);
                generator.addCodeBlock(matchFound);
                breakText = new CodeBlock("break;", 45);
                generator.addCodeBlock(breakText);

                returnText = new CodeBlock("return;", 15);
                generator.addCodeBlock(returnText);

                VBox codetainer = new VBox();
                codetainer.setBackground(new Background(new BackgroundFill(Color.INDIGO, null, null)));

                codetainer.getChildren().addAll(generator.getCodeBlocks());
                titledPane = new TitledPane("Code", codetainer);

                algoTracerContainer = new VBox();
                algoTracerContainer.setPrefWidth(pane.getLayoutBounds().getWidth()/2);
                algoTracerContainer.setLayoutX(indentX);
                algoTracerContainer.setLayoutY(indentY);

                algoTracerContainer.getChildren().add(titledPane);
                algoTracer.getChildren().add(algoTracerContainer);

                // ------------- END OF ALGO TRACER -----------------
            }

            int search() {
                // must run once
                codeAnims.add(Transitions.createHighlighter(indexText.getRect(), "code", null));
                codeAnims.add(Transitions.createHighlighter(init.getRect(), "code", null));
                // must run at least once
                FillTransition counterCompAnim = Transitions.createHighlighter(counterComp.getRect(), "code", null);
                codeAnims.add(counterCompAnim);

                for(int i = 0; i < inputArray.size(); i++) {
                    FillTransition keyCompAnim = Transitions.createHighlighter(keyComp.getRect(), "code", null);
                    codeAnims.add(keyCompAnim);

                    FillTransition charBlockAnim = Transitions.createHighlighter(charBlocks[i].getRect(), "char", null);
                    charBlockAnims.add(charBlockAnim);

                    FillTransition keyFillAnim = Transitions.createHighlighter(keyBlock.getRect(), "char", null);
                    charBlockAnims.add(keyFillAnim);

                    counterCompAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        keyFillAnim.play();
                        playingQueue.add(keyFillAnim);
                        keyCompAnim.play();
                        playingQueue.add(keyCompAnim);
                        charBlockAnim.play();
                        playingQueue.add(charBlockAnim);
                    });

                    if(inputArray.get(i) == key) {
                        keyFillAnim.setToValue(Color.LAWNGREEN);
                        keyFillAnim.setCycleCount(1);

                        charBlockAnim.setToValue(Color.LAWNGREEN);
                        charBlockAnim.setCycleCount(1);

                        codeAnims.add(Transitions.createHighlighter(matchFound.getRect(), "code", null));
                        codeAnims.add(Transitions.createHighlighter(breakText.getRect(), "code", null));
                        this.setIndex(i);
                        break;
                    }
                    // if it reaches here, it means it didn't find a match
                    double distance;

                    // if this is the last element, this means we haven't found a match and we shouldn't create any new translation animations
                    if(i + 1 == inputArray.size()) {
                        distance = pointerWidth * 3;
                    } else {
                        distance = indexTextCentres.get(i + 1) - indexTextCentres.get(i);
                    }

                    TranslateTransition movePointer = Transitions.translateX(pointer, distance, null);
                    translateTransitions.add(movePointer);

                    charBlockAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        movePointer.play();
                        playingQueue.add(movePointer);
                    });
                    codeAnims.add(Transitions.createHighlighter(increment.getRect(), "code", null));
                    counterCompAnim = Transitions.createHighlighter(counterComp.getRect(), "code", null);
                    codeAnims.add(counterCompAnim);
                }
                // must return something at the end of the loop, whether it broke out or not
                codeAnims.add(Transitions.createHighlighter(returnText.getRect(), "code", null));
                return index;
            }

            public void handleOnFinished() {
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

                // Handle translation animations
                for(TranslateTransition tt: translateTransitions) {
                    if(tt.getOnFinished() == null) {
                        tt.setOnFinished(e -> playingQueue.poll());
                    }
                }

                // Handle Match-not-found animations
                if(this.getIndex() == -1) {
                    FillTransition lastIfAnim = codeAnims.get(codeAnims.size() - 2);
                    // get the last code animation
                    FillTransition returnAnim = codeAnims.get(codeAnims.size() - 1);
                    // create new char block anims: the key block and all block so we can paint all the blocks red
                    FillTransition keyBlockNoMatchAnim = Transitions.createHighlighter(keyBlock.getRect(), "char", null);
                    keyBlockNoMatchAnim.setToValue(Color.RED);
                    keyBlockNoMatchAnim.setCycleCount(1);

                    lastIfAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        keyBlockNoMatchAnim.play();
                        playingQueue.add(keyBlockNoMatchAnim);
                        returnAnim.play();
                        playingQueue.add(returnAnim);
                        // get all code blocks and animate their colours to RED
                        for(int i = 0; i < charBlocks.length; i++) {
                            FillTransition elementBlockNoMatchAnim = Transitions.createHighlighter(charBlocks[i].getRect(), "char", null);
                            elementBlockNoMatchAnim.setToValue(Color.RED);
                            elementBlockNoMatchAnim.setCycleCount(1);
                            charBlockAnims.add(elementBlockNoMatchAnim);
                            elementBlockNoMatchAnim.play();
                            playingQueue.add(elementBlockNoMatchAnim);
                        }
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
            }

            private void setAllAnimationsSpeed() {
                double speedFactor = speedSlider.valueProperty().doubleValue();
                for (Transition animation : allAnimations) {
                    animation.setRate(speedFactor);
                }
            }

            void initControls() {
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

                // reset all Char block colours
                keyBlock.getRect().setFill(Color.ORANGE);
                for (CharBlock charBlock : charBlocks) {
                    charBlock.getRect().setFill(Color.ORANGE);
                }
            }
            public void generateNew() {
                clear();

                this.setIndex(-1);
                this.resetFlag = false;
                this.initMainArea();
                this.initAlgoTracer();
                this.search();
                this.handleOnFinished();
                this.setAllAnimations();
                this.setAllAnimationsSpeed();
                this.start();
                // generate new chars and change the codeblock text value
            }

            void reset() {
                clear();

                this.resetFlag = true;
                this.initMainArea();
                this.initAlgoTracer();
                this.search();
                this.handleOnFinished();
                this.setAllAnimations();
                this.setAllAnimationsSpeed();
                this.start();
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
        Visualise linearSearchVis = new Visualise();

        linearSearchVis.initMainArea();
        linearSearchVis.initAlgoTracer();
        linearSearchVis.search();
        linearSearchVis.handleOnFinished();
        linearSearchVis.setAllAnimations();
        linearSearchVis.initControls();
        linearSearchVis.start();

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);
        restartButton.setOnMouseClicked(e -> linearSearchVis.reset());

        Button newSearchButton = new Button("New search");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);
        newSearchButton.setOnMouseClicked(e -> linearSearchVis.generateNew());

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);
        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);
        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);

    }

    public Scene getScene() {
        return scene;
    }
}

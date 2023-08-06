package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.components.SpeedSlider;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.RandomGenerator;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;


public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("My First JavaFX app!");
        stage.setScene(scene);*/

        Pane pane = new Pane();
        Group algoTracer = new Group();
        Group mainArea = new Group();
        Scene scene = new Scene(pane, 1366, 700, Color.GHOSTWHITE);
        Stage stage = new Stage();
        final int SCREENCENTER_X = (int) pane.getLayoutBounds().getCenterX();
        final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
        mainArea.setLayoutX(20);

        Font font = new Font("Consolas", 20);

        final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
        VBox holder = new VBox();
        holder.setPrefWidth(pane.getLayoutBounds().getWidth()/2);

        holder.setLayoutX(indentX);
        holder.setLayoutY((pane.getLayoutBounds().getCenterY()) - 50);

        HBox controls = new HBox();

        class LinearSearch {
            int index = -1;
            ArrayList<Comparable> inputArray = RandomGenerator.generateRandomCharacters( 10);
            Comparable key = RandomGenerator.generateRandomCharacter();
            CharBlock[] charBlocks = new CharBlock[inputArray.size()];
            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList <FillTransition> codeAnim = new ArrayList<>();
            ArrayList <Transition> translateTransitions = new ArrayList<>();
            CharBlock keyBlock;
            Polygon pointer;
            double initialPointerX;
            double initialPointerY;
            double pointerWidth;
            int blockIndent = 60;
            
            CodeBlock indexText, fortext, init, counterComp, increment, openingFor, keyComp, matchFound, breakText, returnText;
            Text methodDef;

            void initMainArea() {
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

                    charBlocks[c] = charBlock;

                    mainArea.getChildren().add(charBlock.getBlock());
                    mainArea.getChildren().add(indexText);
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
                initialPointerX = charBlocks[0].getBlockText().getLayoutBounds().getMinX();
                initialPointerY = SCREENCENTER_Y - 20;

                mainArea.getChildren().addAll(keyBlock.getBlock(), keyLabel, pointer);
            }
            
            void initAlgoTracer() {
                // ------------------- ALGO TRACER --------------------------------
                CodeBlockGenerator generator = new CodeBlockGenerator();

                methodDef = new Text( "linearSearch(key, arr)");
                methodDef.setFont(new Font("Consolas", 20));
                methodDef.setFill(Color.GOLDENROD);

                indexText = new CodeBlock("index = -1;", 15);
                generator.addCodeBlock(indexText);

                fortext = new CodeBlock("for(", 25);
                init = new CodeBlock("i = 0;");
                counterComp = new CodeBlock("i < arr.length;");
                increment = new CodeBlock("i++");
                openingFor = new CodeBlock(")");
                ArrayList<CodeBlock> forLoopHeader1 = new ArrayList<>(Arrays.asList(fortext, init, counterComp, increment, openingFor));

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

                codetainer.getChildren().add(methodDef);

                codetainer.getChildren().addAll(generator.getCodeBlocks());
                TitledPane titledPane = new TitledPane("Code", codetainer);

                holder.getChildren().add(titledPane);
                algoTracer.getChildren().add(holder);

                // ------------- END OF ALGO TRACER -----------------
            } 
            
            int search() {
                // must run once
                codeAnim.add(createHighlighter(indexText.getRect(), Color.INDIGO, Color.BLACK));
                codeAnim.add(createHighlighter(init.getRect(), Color.INDIGO, Color.BLACK));
                // must run at least once
                FillTransition counterCompAnim = createHighlighter(counterComp.getRect(), Color.INDIGO, Color.BLACK);
                codeAnim.add(counterCompAnim);

                for(int i = 0; i < inputArray.size(); i++) {
                    FillTransition keyCompAnim = createHighlighter(keyComp.getRect(), Color.INDIGO, Color.BLACK);
                    codeAnim.add(keyCompAnim);

                    FillTransition charBlockAnim = createHighlighter(charBlocks[i].getRect(), Color.ORANGE, Color.RED);
                    charBlockAnims.add(charBlockAnim);

                    FillTransition keyFillAnim = createHighlighter(keyBlock.getRect(), Color.ORANGE, Color.RED);

                    counterCompAnim.setOnFinished(e -> {
                        keyFillAnim.play();
                        keyCompAnim.play();
                        charBlockAnim.play();
                    });

                    if(inputArray.get(i) == key) {
//                        keyFillAnim.setFromValue(Color.LAWNGREEN);
                        keyFillAnim.setToValue(Color.LAWNGREEN);
                        keyFillAnim.setCycleCount(1);

                        charBlockAnim.setToValue(Color.LAWNGREEN);
                        charBlockAnim.setCycleCount(1);

                        codeAnim.add(createHighlighter(matchFound.getRect(), Color.INDIGO, Color.BLACK));
                        codeAnim.add(createHighlighter(breakText.getRect(), Color.INDIGO, Color.BLACK));
                        index = i;
                        break;
                    }
                    // if it reaches here, it means it didn't find a match

                    // if this is the last element, this means we haven't found a match and we shouldn't create any new translation animations
                    if(i + 1 == inputArray.size()) {
                        keyFillAnim.setToValue(Color.RED);
                        keyFillAnim.setCycleCount(1);

                        charBlockAnim.setToValue(Color.RED);
                        charBlockAnim.setCycleCount(1);

                        break;
                    }

                    TranslateTransition movePointer = translateX(pointer, charBlocks[i].getBlockText().getX() - pointerWidth, charBlocks[i+1].getBlockText().getX() - pointerWidth, 500);
                    translateTransitions.add(movePointer);

                    charBlockAnim.setOnFinished(e -> {
                        movePointer.play();
                    });
                    codeAnim.add(createHighlighter(increment.getRect(), Color.INDIGO, Color.BLACK));
                    counterCompAnim = createHighlighter(counterComp.getRect(), Color.INDIGO, Color.BLACK);
                    codeAnim.add(counterCompAnim);
                }
                // must return something at the end of the loop, whether it broke out or not
                codeAnim.add(createHighlighter(returnText.getRect(), Color.INDIGO, Color.BLACK));
                return index;
            }

            public void handleOnFinished() {
                for(int count = 0; count < codeAnim.size() - 1; count++) {
                    FillTransition currentAnim = codeAnim.get(count);
                    if(currentAnim.getOnFinished() != null) {
                        continue;
                    }
                    FillTransition nextAnim = codeAnim.get(count + 1);
                    currentAnim.setOnFinished(e -> nextAnim.play());
                }
            }

            void initControls() {
                final double MIN_SPEED = 0.25;
                final double MID_SPEED = 1.0;
                final double MAX_SPEED = 2.0;
                Slider speedSlider = new Slider(MIN_SPEED, MAX_SPEED, MID_SPEED);
                speedSlider.setShowTickMarks(true);
                speedSlider.setShowTickLabels(true);
                speedSlider.setMajorTickUnit(0.25);
                speedSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                    double speedFactor = Math.abs(newValue.doubleValue());
                    ArrayList<Transition> transitions = new ArrayList<>();
                    transitions.addAll(translateTransitions);
                    transitions.addAll(codeAnim);
                    transitions.addAll(charBlockAnims);
                    for(int i = 0; i < transitions.size(); i++) {
                        transitions.get(i).setRate(speedFactor);
                    }
                });

                speedSlider.setLabelFormatter(new StringConverter<Double>() {
                    @Override
                    public String toString(Double n) {
                        if (n == MAX_SPEED) {
                            return "Fast";
                        } else if (n == MID_SPEED) {
                            return "Normal";
                        } else if (n == MIN_SPEED) {
                            return "Slow";
                        } else {
                            return ""; // Return an empty string for other values
                        }
                    }

                    @Override
                    public Double fromString(String s) {
                        return null;
                    }
                });

                controls.getChildren().add(speedSlider);
            }
            public void generateNew() {
                // generate new key and update on screen
                // generate new chars and change the codeblock text value
            }

            void reset() {
                // translate the key block and key label back to the position of the first element, then start the codeAnimation
                pointer.setTranslateX(charBlocks[0].getBlockText().getLayoutBounds().getMinX() - pointerWidth);

                ArrayList<Transition> transitions = new ArrayList<>();
                transitions.addAll(translateTransitions);
                transitions.addAll(codeAnim);
                transitions.addAll(charBlockAnims);
                for(int i = 0; i < transitions.size(); i++) {
                    transitions.get(i).stop();
                }

                if(this.getIndex() != -1) {
                    // reset key block and matched block colours
                    CharBlock matchedBlock = (CharBlock) Array.get(charBlocks, this.getIndex());
                    keyBlock.getRect().setFill(Color.ORANGE);
                    matchedBlock.getRect().setFill(Color.ORANGE);
                } else {
                    CharBlock lastUnmatchedBlock = (CharBlock) Array.get(charBlocks, charBlocks.length - 1);
                    keyBlock.getRect().setFill(Color.ORANGE);
                    lastUnmatchedBlock.getRect().setFill(Color.ORANGE);
                }

                codeAnim.get(0).play();
            }

            void start() {
                codeAnim.get(0).play();
            }

            public int getIndex() {
                return index;
            }
        }
        LinearSearch linearSearchVis = new LinearSearch();

        linearSearchVis.initMainArea();
        linearSearchVis.initAlgoTracer();
        linearSearchVis.search();
        linearSearchVis.handleOnFinished();
        linearSearchVis.initControls();
        linearSearchVis.start();

        VBox buttonContainer = new VBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);
        restartButton.setOnMouseClicked(e -> linearSearchVis.reset());
        buttonContainer.getChildren().add(restartButton);

        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);
        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);

        stage.setScene(scene);
        stage.show();
    }

    static FillTransition createHighlighter(Shape node, Color startColor, Color endColor) {
        FillTransition ft = new FillTransition();
        ft.setShape(node);
        ft.setFromValue(startColor);
        ft.setToValue(endColor);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setDuration(Duration.millis(200));
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        return ft;
    }

    static ScaleTransition scaleBlock(Shape block, int duration) {
        ScaleTransition st = new ScaleTransition(Duration.millis(duration), block);
        st.fromZProperty();
        return st;
    }

    static TranslateTransition translateX(Node node, double start, double end, double duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(start);
        translateTransition.setToX(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    static TranslateTransition translateY(Node node, double start, double end, double duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromY(start);
        translateTransition.setToY(end);
        translateTransition.setCycleCount(1);

        return translateTransition;
    }

    public static void main(String[] args) {
        launch();
    }

}
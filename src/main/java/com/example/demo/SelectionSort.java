package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.components.PlayingQueue;
import com.example.demo.utils.CodeBlockGenerator;
import com.example.demo.utils.RandomGenerator;
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
            CharBlock[] charBlocks;
            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList<Text> indexTexts = new ArrayList<>();
            ArrayList<Double> indexTextCentres = new ArrayList<>();

            ArrayList<Text> charBlockTexts = new ArrayList<>();
            ArrayList<Double> charBlockTextCentres = new ArrayList<>();
            ArrayList <FillTransition> codeAnims = new ArrayList<>();
            ArrayList <TranslateTransition> translateTransitions = new ArrayList<>();
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
                inputArray = inputArray.isEmpty() || !resetFlag ? RandomGenerator.generateRandomNumbers(5, 100, 10) : inputArray;
                charBlocks = new CharBlock[inputArray.size()];
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

            void sort(){
                int min = 0;
                Comparable temp;
                for(int i = 0; i < inputArray.size() - 1; i++){
                    min = i;
                    for(int j = i + 1; j < inputArray.size(); j++){
                        if(inputArray.get(j).compareTo(inputArray.get(min)) < 0) {
                            min = j;
                        }
                    }
                    // hold new minimum value
                    temp = inputArray.get(min);
                    // set old minimum to its new position
                    inputArray.set(inputArray.indexOf(temp), inputArray.get(i));
                    // set new minimum
                    inputArray.set(i, temp);
                }
            }
        }

        Visualise selectionSortVis = new Visualise();
        selectionSortVis.initAlgoTracer();
        selectionSortVis.initMainArea();
        selectionSortVis.sort();

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);

        Button newSearchButton = new Button("New sort");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);
        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);

        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);
    }

    public Scene getScene() {
        return scene;
    }
}

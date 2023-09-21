package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import com.example.demo.components.PlayingQueue;
import com.example.demo.components.SpeedSlider;
import com.example.demo.utils.CodeBlockGenerator;
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
import javafx.util.Duration;

import java.lang.reflect.Array;

import java.util.*;

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
        HBox controls = new HBox();


        class Visualiser {
            int index = -1;

            Slider speedSlider;
            boolean resetFlag = false;
            ArrayList<Comparable> inputArray = new ArrayList<>();
            Comparable key = null;

            ArrayList <FillTransition> charBlockAnims = new ArrayList<>();
            ArrayList <FillTransition> codeAnims = new ArrayList<>();
            ArrayList <TranslateTransition> translateTransitions = new ArrayList<>();
            // special case transitions
            ArrayList<TranslateTransition> moveMiddleTransitions = new ArrayList<>();
            ArrayList<FadeTransition> fadeTransitions = new ArrayList<>();
            ArrayList<Transition> allAnimations = new ArrayList<>();

            Queue<Transition> playingQueue = new PlayingQueue().getPlayingQueue();
            ArrayList<Text> indexTexts = new ArrayList<>();
            ArrayList<Double> indexTextCentres = new ArrayList<>();
            CharBlock[] charBlocks;
            CharBlock keyBlock;
            Group startPointer, midPointer, endPointer;
            Polygon startPointerShape, midPointerShape, endPointerShape;
            Text startPointerLabel, midPointerLabel, endPointerLabel;
            double initialStartPointerX, initialMidPointerX, initialEndPointerX;
            double pointerWidth, pointerHeight;

            Group mainAreaContainer;
            CodeBlock methodDef, startendDeclaration, whileText, whileCondition, closingParen, divider, keyEquals, matchFound, keyLess, goLeft, elseText, goRight, noMatch;
            VBox algoTracerContainer;
            TitledPane titledPane;
            final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
            final int indentY = (int) (pane.getLayoutBounds().getCenterY()) - 50;

            void initMainArea() {
                inputArray = inputArray.isEmpty() || !resetFlag ? RandomGenerator.generateRandomNumbers(5, 20, 10) : inputArray;
                Collections.sort(inputArray);

                charBlocks = new CharBlock[inputArray.size()];
                key = key == null || !resetFlag ? RandomGenerator.generateRandomNumber(5, 15) : key;
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
                initialStartPointerX = startPointer.localToScene(startPointer.getBoundsInLocal()).getMinX();


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
                initialMidPointerX = startPointer.localToScene(midPointer.getBoundsInLocal()).getMinX();

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
                initialEndPointerX = endPointer.localToScene(endPointer.getBoundsInLocal()).getMinX();

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

                algoTracerContainer.setLayoutX(indentX);
                algoTracerContainer.setLayoutY(indentY);

                algoTracerContainer.getChildren().add(titledPane);
                algoTracer.getChildren().add(algoTracerContainer);
            }

            int search() {
                int start = 0, end = inputArray.size() - 1, middle = (start + end) / 2;

                FillTransition whileConditionAnim = Transitions.createHighlighter(whileCondition.getRect(), "code", null);
                codeAnims.add(whileConditionAnim);

                FadeTransition fadeMidPointer = Transitions.fadeItemIn(midPointer, null);
                // we're multiplying by 2 because the CodeBlock animations have a cycle count of 2. In other words, they take twice the time of their specified duration to complete
                fadeMidPointer.setDelay(whileConditionAnim.getDuration().multiply(2));
                fadeTransitions.add(fadeMidPointer);

                String startPointerLevel = "BOTTOM";
                String midPointerLevel = "BOTTOM";
                String endPointerLevel = "BOTTOM";

                while (start <= end) {
                    FillTransition dividerAnim = Transitions.createHighlighter(divider.getRect(), "code", null);
                    codeAnims.add(dividerAnim);

                    int oldMiddle = middle;
                    String oldMiddleLevel = midPointerLevel;
                    middle = (start + end) / 2;
                    double distanceFromNewMiddle = indexTextCentres.get(middle) - indexTextCentres.get(oldMiddle);

                    if(start != 0 || end != inputArray.size() - 1) {
                        double yTranslation;
                        double levelDistance = 0;
                        if(middle == end && middle == start) {
                            // from centre to top, or bottom to top
                            levelDistance = oldMiddleLevel.equals("CENTRE") ? -(pointerHeight)  : -(pointerHeight * 2);
                            midPointerLevel = "TOP";
                            yTranslation = levelDistance;
                        } else if(middle == end || middle == start) {
                            // from bottom to centre
                            midPointerLevel = "CENTRE";
                            yTranslation = -(pointerHeight);
                        } else {
                            if(oldMiddleLevel.equals("TOP")) {
                                // go down two levels
                                yTranslation = pointerHeight * 2;
                            } else if (oldMiddleLevel.equals("CENTRE")){
                                // go down one level
                                yTranslation = pointerHeight;
                            } else {
                                yTranslation = 0;
                            }
                            midPointerLevel = "BOTTOM";
                        }
                        fadeMidPointer = Transitions.fadeItemIn(midPointer, null);
                        fadeTransitions.add(fadeMidPointer);
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
                        playingQueue.poll();
                        keyEqualsAnim.play();
                        playingQueue.add(keyEqualsAnim);
                        keyBlockCheckAnim.play();
                        playingQueue.add(keyBlockCheckAnim);
                        checkBlockAnim.play();
                        playingQueue.add(keyBlockCheckAnim);
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
                        FillTransition nextWhileAnim = Transitions.createHighlighter(whileCondition.getRect(), "code", null);

                        if (inputArray.get(middle).compareTo(key) > 0) {
                            double distance;
                            double yTranslation = 0;
                            // the only time it's ever going to be at the centre is if it was equal to the start pointer
                            if (endPointerLevel.equals("CENTRE")) {
                                yTranslation = pointerHeight;
                                endPointerLevel = "BOTTOM";
                            }
                            // move pointer up if start and end are(going to be) equal
                            if(end - 1 == start || middle - 1 == 0) {
                                yTranslation = -(pointerHeight);
                                endPointerLevel = "CENTRE";
                            }

                            // IF end - 1 < start, a.k.a No Match was found.
                            if(end - 1 < start) {
                                fadeMidPointer = null;
                                // if it passes the left edge
                                if (middle - 1 < 0) {
                                    // negating it so the translate X can move left
                                    distance = -(pointerWidth * 3);
                                } else {
                                    distance = indexTextCentres.get(middle - 1) - indexTextCentres.get(end);
                                }
                            } else {
                                // search continues
                                distance = indexTextCentres.get(middle - 1) - indexTextCentres.get(end);
                                fadeMidPointer = Transitions.fadeItemOut(midPointer, null);
                                fadeTransitions.add(fadeMidPointer);
                            }

                            TranslateTransition goLeftAnim = Transitions.translateX(endPointer, distance, 200);
                            goLeftAnim.setByY(yTranslation);

                            translateTransitions.add(goLeftAnim);
                            FadeTransition finalFadeMidPointer1 = fadeMidPointer;

                            FillTransition keyLessAnim = Transitions.createHighlighter(keyLess.getRect(), "code", null);
                            FillTransition goLeftCodeAnim = Transitions.createHighlighter(goLeft.getRect(), "code", null);
                            codeAnims.add(keyLessAnim);
                            codeAnims.add(goLeftCodeAnim);
                            codeAnims.add(nextWhileAnim);

                            int finalMiddle = middle;
                            keyLessAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                goLeftCodeAnim.play();
                                playingQueue.add(goLeftCodeAnim);
                                goLeftAnim.play();
                                playingQueue.add(goLeftAnim);
                                if(finalFadeMidPointer1 != null) {
                                    finalFadeMidPointer1.play();
                                    playingQueue.add(finalFadeMidPointer1);
                                }
                                // Grey out the areas we're not searching anymore
                                for(int i = finalMiddle; i < charBlocks.length; i++) {
                                    charBlocks[i].getRect().setFill(Color.GREY);
                                }
                            });

                            checkBlockAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                keyLessAnim.play();
                                playingQueue.add(keyLessAnim);
                            });
                            end = middle - 1;
                        } else {
                            double distance;
                            double yTranslation = 0;
                            // the only time it's ever going to be at the centre is if it has crossed the end pointer
                            if(startPointerLevel.equals("CENTRE")) {
                                yTranslation = pointerHeight;
                                startPointerLevel = "BOTTOM";
                            }
                            // move pointer up if start and end are(going to be) equal
                            if(start + 1 == end || middle + 1 == end) {
                                yTranslation = -(pointerHeight);
                                startPointerLevel = "CENTRE";
                            }

                            // IF start + 1 > end should be the main condition. Then the edge stuff should be inside
                            if(start + 1 > end) {
                                fadeMidPointer = null;
                                if (middle + 1 == inputArray.size()) {
                                    distance = (pointerWidth * 3);
                                } else {
                                    distance = indexTextCentres.get(middle + 1) - indexTextCentres.get(start);
                                }

                            } else {
                                distance = indexTextCentres.get(middle + 1) - indexTextCentres.get(start);
                                fadeMidPointer = Transitions.fadeItemOut(midPointer, null);
                                fadeTransitions.add(fadeMidPointer);
                            }

                            TranslateTransition goRightAnim = Transitions.translateX(startPointer, distance, 200);
                            goRightAnim.setByY(yTranslation);
                            translateTransitions.add(goRightAnim);
                            FadeTransition finalFadeMidPointer = fadeMidPointer;
                            FillTransition goRightCodeAnim = Transitions.createHighlighter(goRight.getRect(), "code", null);
                            codeAnims.add(goRightCodeAnim);
                            codeAnims.add(nextWhileAnim);

                            int finalMiddle1 = middle;
                            checkBlockAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                goRightAnim.play();
                                playingQueue.add(goRightAnim);
                                goRightCodeAnim.play();
                                playingQueue.add(goRightCodeAnim);
                                // Grey out the areas we're not searching anymore
                                for(int i = 0; i <= finalMiddle1; i++) {
                                    charBlocks[i].getRect().setFill(Color.GREY);
                                }
                            });

                            goRightCodeAnim.setOnFinished(e-> {
                                playingQueue.poll();
                                nextWhileAnim.play();
                                playingQueue.add(nextWhileAnim);
                                if(finalFadeMidPointer != null) {
                                    finalFadeMidPointer.play();
                                    playingQueue.add(finalFadeMidPointer);
                                }
                            });

                            start = middle + 1;
                        }
                    }
                }

                codeAnims.add(Transitions.createHighlighter(noMatch.getRect(), "code", null));
                return -1;
            }

            public void handleOnFinished() {
                // HANDLE ONFINISHED FOR CODE ANIMS
                int moveMiddleSize = moveMiddleTransitions.size();
                int moveMiddleArrayCounter = 0;
                // we're not looping through the whole codeAnim array because the last while codeAnim will handle the RED match-not-found char block animation
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
                            // add Fade In anims - moveMiddle size and the number of fadeIn transitions to add are always equal
                            ArrayList<Integer> fadeInAnims = new ArrayList<>();
                            for(int i = 1; i < fadeTransitions.size(); i++) {
                                if(fadeTransitions.get(i).getToValue() == 1) {
                                    fadeInAnims.add(i);
                                }
                            }
                            currentAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                nextCodeAnim.play();
                                playingQueue.add(nextCodeAnim);
                                fadeTransitions.get(fadeInAnims.get(finalMoveMiddleArrayCounter)).play();
                                playingQueue.offer(fadeTransitions.get(fadeInAnims.get(finalMoveMiddleArrayCounter)));
                                moveMiddleTransitions.get(finalMoveMiddleArrayCounter).play();
                                playingQueue.add(moveMiddleTransitions.get(finalMoveMiddleArrayCounter));
                            });
                            moveMiddleArrayCounter++;
                        } else {
                            // handle any whileConditionAnim that should not have a moveMiddle anim after it
                            // check if you can modify the onFinished using getOnFinished
                            currentAnim.setOnFinished(e -> {
                                playingQueue.poll();
                                nextAnim.play();
                                playingQueue.add(nextAnim);
                            });
                        }
                    } else {
                        // handle any other codeblock animations
                        // repeating for semantic
                        currentAnim.setOnFinished(e -> {
                            playingQueue.poll();
                            nextAnim.play();
                            playingQueue.add(nextAnim);
                        });
                    }
                }
                //---

                // Handle Translation animation onFinished
                for(TranslateTransition tt: translateTransitions) {
                    if(tt.getOnFinished() == null) {
                        tt.setOnFinished(e -> playingQueue.poll());
                    }
                }

                // Handle fade animations onFinished
                for(FadeTransition ft: fadeTransitions) {
                    if(ft.getOnFinished() == null) {
                        ft.setOnFinished(e -> playingQueue.poll());
                    }
                }

                // Handle Match-Not-Found animations
                if(this.getIndex() == -1) {
                    FillTransition lastWhileAnim = codeAnims.get(codeAnims.size() - 2);
                    // get the last code animation
                    FillTransition noMatchCodeAnim = codeAnims.get(codeAnims.size() - 1);
                    // create new char block anims: the key block and all block so we can paint all the blocks red
                    FillTransition keyBlockNoMatchAnim = Transitions.createHighlighter(keyBlock.getRect(), "char", null);
                    keyBlockNoMatchAnim.setToValue(Color.RED);
                    keyBlockNoMatchAnim.setCycleCount(1);

                    lastWhileAnim.setOnFinished(e -> {
                        playingQueue.poll();
                        keyBlockNoMatchAnim.play();
                        playingQueue.add(keyBlockNoMatchAnim);
                        noMatchCodeAnim.play();
                        playingQueue.add(noMatchCodeAnim);
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
                moveMiddleTransitions.clear();
                codeAnims.clear();
                charBlockAnims.clear();
                fadeTransitions.clear();

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
            }

            public void reset() {
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
        binarySearchVis.setAllAnimations();
        binarySearchVis.initControls();
        binarySearchVis.start();

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutX(10);
        buttonContainer.setLayoutY(20);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setPrefHeight(20);
        restartButton.setOnMouseClicked(e -> binarySearchVis.reset());

        Button newSearchButton = new Button("New search");
        newSearchButton.setPrefWidth(150);
        newSearchButton.setPrefHeight(20);
        newSearchButton.setOnMouseClicked(e -> binarySearchVis.generateNew());

        buttonContainer.getChildren().addAll(restartButton, newSearchButton);

        // position controls
        controls.relocate(20, mainArea.getLayoutBounds().getMaxY() + 50);
        pane.getChildren().addAll(mainArea, algoTracer, buttonContainer, controls);
    }

    public Scene getScene() {
        return scene;
    }

}

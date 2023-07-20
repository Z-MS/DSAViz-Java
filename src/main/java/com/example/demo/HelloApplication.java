package com.example.demo;

import com.example.demo.components.CharBlock;
import com.example.demo.components.CodeBlock;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;


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
        final int SCREENCENTER_Y = (int) pane.getLayoutBounds().getCenterY();
        mainArea.setLayoutX(20);
        Font font = new Font("Consolas", 20);
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Rectangle[] numBlocks = new Rectangle[nums.length];
        ArrayList <FillTransition> numBlockAnims = new ArrayList<>();
        int blockIndent = 60;
        // ------------------- MAIN AREA - SEARCH BLOCKS ------------------

        CharBlock keyBlock = new CharBlock(0,SCREENCENTER_Y - 65,60,"3",null,40);
        keyBlock.getRect().setStroke(Color.BLACK);
        keyBlock.getRect().setArcWidth(5);
        keyBlock.getRect().setArcHeight(5);

        Text keyLabel = new Text(keyBlock.getBlockText().getLayoutBounds().getMinX() - 10, keyBlock.getRect().getLayoutBounds().getMinY() - 10, "key");
        keyLabel.setFill(Color.BLACK);
        keyLabel.setFont(new Font("Consolas", 30));


        mainArea.getChildren().add(keyBlock.getBlock());
        mainArea.getChildren().add(keyLabel);

        for(int c = 0; c < nums.length; c++) {
            CharBlock charBlock = new CharBlock((blockIndent + 15) * c, SCREENCENTER_Y, 60, ""+ nums[c], null, 40);
            Rectangle rect = charBlock.getRect();
            rect.setStroke(Color.BLACK);
            rect.setArcWidth(5);
            rect.setArcHeight(5);

            Text indexText = new Text("" + c);
            indexText.setFont(new Font("Consolas", 30));
            indexText.setFill(Color.ROSYBROWN);
            indexText.setX(rect.getLayoutBounds().getCenterX() - indexText.getLayoutBounds().getCenterX());
            indexText.setY(rect.getLayoutBounds().getMaxY() + 30);

            numBlocks[c] = rect;

//            mainArea.getChildren().add(rect);
//            mainArea.getChildren().add(blockText);
            mainArea.getChildren().add(charBlock.getBlock());
            mainArea.getChildren().add(indexText);
        }
        // ------------------- END OF MAIN AREA - SEARCH BLOCKS


        // ------------------- ALGO TRACER --------------------------------
        final int indentX = (int) pane.getLayoutBounds().getCenterX() + 250;
        final int indentY = (int) pane.getLayoutBounds().getCenterY();
        final int spacing = 20;

        Text methodDef = new Text(indentX, indentY - 5, "linearSearch(key, arr)");
        methodDef.setFont(new Font("Consolas", 20));
        methodDef.setFill(Color.GOLDENROD);

        CodeBlock indexText = new CodeBlock(indentX + spacing, indentY, "index = -1;");

        Text fortext = new Text(indentX + spacing, indentY + spacing * 1.70, "for(");
        fortext.setFont(new Font("Consolas", 20));
        fortext.setFill(Color.WHEAT);
        CodeBlock init = new CodeBlock(fortext.getLayoutBounds().getMaxX(), indentY + spacing, "i = 0;");
        CodeBlock counterComp = new CodeBlock(init.getRect().getLayoutBounds().getMaxX() + 5, indentY + spacing, "i < arr.length;");
        CodeBlock increment = new CodeBlock(counterComp.getRect().getLayoutBounds().getMaxX() + 5, indentY + spacing, "i++");
        Text openingFor = new Text(increment.getRect().getLayoutBounds().getMaxX() + 5, indentY + spacing * 1.75, ")");
        openingFor.setFont(font);
        openingFor.setFill(Color.WHEAT);

        CodeBlock keyComp = new CodeBlock(indentX + spacing * 2, openingFor.getLayoutBounds().getMaxY(), "if(arr[i] == key)");
        CodeBlock matchFound = new CodeBlock(indentX + spacing * 4, keyComp.getRect().getLayoutBounds().getMaxY(), "index = i;");
        CodeBlock breakText = new CodeBlock(indentX + spacing * 4, matchFound.getRect().getLayoutBounds().getMaxY(), "break;");

        CodeBlock returnText = new CodeBlock(indentX + spacing, breakText.getRect().getLayoutBounds().getMaxY(), "return;");

        Rectangle tracerBorder = new Rectangle(indentX, (pane.getLayoutBounds().getCenterY()) - 50, pane.getLayoutBounds().getWidth()/2, pane.getLayoutBounds().getHeight()/4 + 50);
        tracerBorder.setStroke(Color.GREY);
        tracerBorder.setFill(Color.INDIGO);

        algoTracer.getChildren().add(tracerBorder);
        algoTracer.getChildren().add(methodDef);
        algoTracer.getChildren().add(indexText.getBlock());
        algoTracer.getChildren().add(fortext);
        algoTracer.getChildren().add(init.getBlock());
        algoTracer.getChildren().add(counterComp.getBlock());
        algoTracer.getChildren().add(increment.getBlock());
        algoTracer.getChildren().add(openingFor);
        algoTracer.getChildren().add(keyComp.getBlock());
        algoTracer.getChildren().add(matchFound.getBlock());
        algoTracer.getChildren().add(breakText.getBlock());
        algoTracer.getChildren().add(returnText.getBlock());
        // ------------- END OF ALGO TRACER -----------------

        pane.getChildren().add(mainArea);
        pane.getChildren().add(algoTracer);

        stage.setScene(scene);
        stage.show();


        ArrayList <FillTransition> codeAnim = new ArrayList<>();


        class LinearSearch {
            int search(int key, int[] arr) {
                int index = -1;

                // must run once
                codeAnim.add(createHighlighter(indexText.getRect(), Color.INDIGO, Color.BLACK));
                codeAnim.add(createHighlighter(init.getRect(), Color.INDIGO, Color.BLACK));
                // must run at least once
                FillTransition counterCompAnim = createHighlighter(counterComp.getRect(), Color.INDIGO, Color.BLACK);
                codeAnim.add(counterCompAnim);

                for(int i = 0; i < arr.length; i++) {
                    FillTransition keyCompAnim = createHighlighter(keyComp.getRect(), Color.INDIGO, Color.BLACK);
                    codeAnim.add(keyCompAnim);

                    FillTransition numBlockAnim = createHighlighter(numBlocks[i], Color.ORANGE, Color.RED);
                    numBlockAnims.add(numBlockAnim);

                    FillTransition keyFillAnim = createHighlighter(keyBlock.getRect(), Color.ORANGE, Color.RED);

                    counterCompAnim.setOnFinished(e -> {
                        keyFillAnim.play();
                        keyCompAnim.play();
                        numBlockAnim.play();
                    });

                    if(arr[i] == key) {
//                        keyFillAnim.setFromValue(Color.LAWNGREEN);
                        keyFillAnim.setToValue(Color.LAWNGREEN);
                        keyFillAnim.setCycleCount(1);

                        numBlockAnim.setToValue(Color.LAWNGREEN);
                        numBlockAnim.setCycleCount(1);

                        codeAnim.add(createHighlighter(matchFound.getRect(), Color.INDIGO, Color.BLACK));
                        codeAnim.add(createHighlighter(breakText.getRect(), Color.INDIGO, Color.BLACK));
                        index = i;
                        break;
                    }
                    // if it reaches here, it means it didn't find a match


                    TranslateTransition moveKeyBlock = translateX(keyBlock.getBlock(), numBlocks[i].getX(), numBlocks[i+1].getX(), 500);
                    TranslateTransition moveLabel = translateX(keyLabel, numBlocks[i].getX(), numBlocks[i + 1].getX(), 500);

                    numBlockAnim.setOnFinished(e -> {
                        moveKeyBlock.play();
                        moveLabel.play();
                    });
                    codeAnim.add(createHighlighter(increment.getRect(), Color.INDIGO, Color.BLACK));
                    counterCompAnim = createHighlighter(counterComp.getRect(), Color.INDIGO, Color.BLACK);
                    codeAnim.add(counterCompAnim);
                }
                return index;
            }
        }
        new LinearSearch().search(3, nums);
        // must return something at the end of the loop, whether it broke out or not
        codeAnim.add(createHighlighter(returnText.getRect(), Color.INDIGO, Color.BLACK));

        for(int count = 0; count < codeAnim.size() - 1; count++) {
            FillTransition currentAnim = codeAnim.get(count);
            if(currentAnim.getOnFinished() != null) {
                continue;
            }
            FillTransition nextAnim = codeAnim.get(count + 1);
            currentAnim.setOnFinished(e -> nextAnim.play());
        }

        codeAnim.get(0).play();
        // numBlockAnims.get(0).play();
    }

    static FillTransition createHighlighter(Shape node, Color startColor, Color endColor) {
        FillTransition ft = new FillTransition();
        ft.setShape(node);
        ft.setFromValue(startColor);
        ft.setToValue(endColor);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setDuration(Duration.millis(500));
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
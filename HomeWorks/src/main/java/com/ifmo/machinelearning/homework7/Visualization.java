package com.ifmo.machinelearning.homework7;

import com.ifmo.machinelearning.visualization.DrawingNumber;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Whiplash on 01.12.2014.
 */
public class Visualization extends DrawingNumber {

    private static final int SCALE = 10;
    private List<NumberImageInstance> test;
    private NumberRecognitionNeuralNet net;
    private int cur = 0;

    @Override
    protected void initialize() {
        test = NumberImageInstance.createFromFiles(Test.TEST_DATA, Test.TEST_LABELS);
        Collections.shuffle(test);
        net = NumberRecognitionNeuralNet.fromDump("HomeWorks/res/homework7/08-12-2014_00.03.17.523_committee_0_500_0.05_0.9275.txt");

        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseEntered(mouseHandler);
        scene.setOnMouseExited(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);

        setLineWidth(SCALE);
        drawNumber(test.get(cur).getValues());
        System.out.println(String.format("Real: %d vs Guess: %d", test.get(cur).getClassId(), net.getNumber(test.get(cur))));
    }

    EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                cur++;
            } else {
                cur += test.size() - 1;
            }
            cur %= test.size();
            drawNumber(test.get(cur).getValues());
            System.out.println(String.format("Real: %d vs Guess: %d", test.get(cur).getClassId(), net.getNumber(test.get(cur))));
        }
    };

    @Override
    protected String getTitle() {
        return "Numbers";
    }

    @Override
    protected double getScale() {
        return SCALE;
    }

    @Override
    protected int getHeight() {
        return 28 * SCALE;
    }

    @Override
    protected int getWidth() {
        return 28 * SCALE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

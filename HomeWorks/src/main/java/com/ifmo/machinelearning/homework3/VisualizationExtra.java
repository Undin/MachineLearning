package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.classifiers.svm.GaussKernel;
import com.ifmo.machinelearning.library.classifiers.svm.SVMClassifier;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class VisualizationExtra extends Application {

    private static final String TITLE = "SVM visualization";
    private static final String[] ATTRIBUTES_NAMES = {"x", "y"};

    private static final double C = 8;
    private static final double GAMMA = 0.5;

    private static final double SCALE = 300;

    private static final int LINE_WIDTH = 2;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");
        SVMClassifier classifier = new SVMClassifier(sample, new GaussKernel(GAMMA), C);
        classifier.training();
        drawValues(canvas.getGraphicsContext2D(), classifier);
        drawValues(canvas.getGraphicsContext2D(), sample);
        primaryStage.show();
    }

    private void drawValues(GraphicsContext gc, SVMClassifier classifier) {
        gc.setFill(Color.BURLYWOOD);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(LINE_WIDTH);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new ClassifiedInstance(ATTRIBUTES_NAMES, new double[]{x, y}, 2)) == 1) {
                    gc.fillOval(x * SCALE + WIDTH / 2, -y * SCALE + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
                } else {
                    gc.strokeOval(x * SCALE + WIDTH / 2, -y * SCALE + HEIGHT / 2, 1, 1);
                }
            }
        }
    }

    private void drawValues(GraphicsContext gc, List<ClassifiedInstance> sample) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(LINE_WIDTH);
        for (ClassifiedInstance point : sample) {
            if (point.getClassId() == 1) {
                gc.fillOval(point.getAttributeValue(0) * SCALE + WIDTH / 2, -point.getAttributeValue(1) * SCALE + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
            } else {
                gc.strokeOval(point.getAttributeValue(0) * SCALE + WIDTH / 2, -point.getAttributeValue(1) * SCALE + HEIGHT / 2, 1, 1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

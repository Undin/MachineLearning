package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.ClassifiedInstance;
import com.ifmo.machinelearning.library.InstanceCreator;
import com.ifmo.machinelearning.library.svm.InnerProductKernel;
import com.ifmo.machinelearning.library.test.Statistics;
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
public class Visualization extends Application {

    private static final String TITLE = "SVM visualization";

    private static final double C = 2D;

    private static final double SCALE = 30;

    private static final int LINE_WIDTH = 4;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        drawAxis(canvas.getGraphicsContext2D());
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));

        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework3/LinearDataset");
        SVMTestMachine testMachine = new SVMTestMachine(sample);
        testMachine.setC(C);
        Statistics statistics = testMachine.test(sample);
        System.out.println(statistics.getFMeasure());

        PointSVMClassifier classifier = new PointSVMClassifier(sample, new InnerProductKernel(), 2);
        classifier.training();
        System.out.println(String.format("y = %f * x + %f", classifier.getK(), classifier.getB()));

        drawLine(canvas.getGraphicsContext2D(), classifier.getK(), classifier.getB());
        drawValues(canvas.getGraphicsContext2D(), sample);
        primaryStage.show();
    }

    private void drawAxis(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
        gc.strokeLine(HEIGHT / 2, 0, HEIGHT / 2, WIDTH);
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

    private void drawLine(GraphicsContext gc, double k, double b) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(0, -((double) -(WIDTH / 2) / SCALE * k + b) * SCALE + HEIGHT / 2, WIDTH, -((double) (WIDTH / 2) / SCALE * k + b) * SCALE + HEIGHT / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.svm.SVMClassifier;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class VisualizationExtra extends Application {

    private static final String TITLE = "SVM visualization";

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

        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("./HomeWorks/res/homework1/chips.txt"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ", ");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }
        Classifier<Point> classifier = new SVMClassifier<>(sample, new RBFKernel(GAMMA), C);
        drawValues(canvas.getGraphicsContext2D(), classifier.training());
        drawValues(canvas.getGraphicsContext2D(), sample);
        primaryStage.show();
    }

    private void drawValues(GraphicsContext gc, Classifier<Point> classifier) {
        gc.setFill(Color.BURLYWOOD);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(LINE_WIDTH);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new Point(x, y)) == 1) {
                    gc.fillOval(x * SCALE + WIDTH / 2, -y * SCALE + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
                } else {
                    gc.strokeOval(x * SCALE + WIDTH / 2, -y * SCALE + HEIGHT / 2, 1, 1);
                }
            }
        }
    }

    private void drawValues(GraphicsContext gc, List<Point> sample) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(LINE_WIDTH);
        for (Point point : sample) {
            if (point.getClassId() == 1) {
                gc.fillOval(point.getX() * SCALE + WIDTH / 2, -point.getY() * SCALE + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
            } else {
                gc.strokeOval(point.getX() * SCALE + WIDTH / 2, -point.getY() * SCALE + HEIGHT / 2, 1, 1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.knn.DistanceWeight;
import com.ifmo.machinelearning.library.knn.KNN;
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
 * Created by Whiplash on 21.09.2014.
 */
public class Visualization extends Application {

    private static final String TITLE = "KNN visualization";

    private static final int LINE_WIDTH = 2;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 20;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        drawAxis(canvas.getGraphicsContext2D());
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("chips.txt"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }

        drawValues(canvas.getGraphicsContext2D(), new KNN<>(sample, ManhattanDistance.getInstance(), DistanceWeight.getInstance(), 6));
    }

    private void drawAxis(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
        gc.strokeLine(HEIGHT / 2, 0, HEIGHT / 2, WIDTH);
    }

    private void drawValues(GraphicsContext gc, Classifier<Point> classifier) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(LINE_WIDTH);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new Point(x, y)) == 1) {
                    gc.fillOval(x * 300 + WIDTH / 2, y * 300 + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
                } else {
                    gc.strokeOval(x * 300 + WIDTH / 2, y * 300 + HEIGHT / 2, 1, 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
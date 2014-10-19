package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.ManhattanDistance;
import com.ifmo.machinelearning.Point;
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
 * Created by Whiplash on 05.10.2014.
 */
public class VisualizationExtra extends Application {

    private static final String TITLE = "SVM visualization";

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
        primaryStage.show();

        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("./HomeWorks/res/homework1/chips.txt"));
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

    private void drawValues(GraphicsContext gc, Classifier<Point> classifier) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
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

    public static void main(String[] args) {
        launch(args);
    }
}

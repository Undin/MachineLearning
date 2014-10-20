package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.classifiers.knn.ManhattanDistance;
import com.ifmo.machinelearning.library.classifiers.knn.DistanceWeight;
import com.ifmo.machinelearning.library.classifiers.knn.KNN;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Whiplash on 21.09.2014.
 */
public class Visualization extends Application {

    private static final String TITLE = "KNN visualization";
    private static final String[] ATTRIBUTES_NAMES = {"x", "y"};

    private static final int LINE_WIDTH = 2;
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
        primaryStage.show();

        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");
        drawValues(canvas.getGraphicsContext2D(), new KNN(sample, new ManhattanDistance(), new DistanceWeight(), 6));
    }

    private void drawAxis(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
        gc.strokeLine(HEIGHT / 2, 0, HEIGHT / 2, WIDTH);
    }

    private void drawValues(GraphicsContext gc, KNN classifier) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(LINE_WIDTH);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new ClassifiedInstance(ATTRIBUTES_NAMES, new double[]{x, y}, 2)) == 1) {
                    gc.fillOval(x * 300 + WIDTH / 2, -y * 300 + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
                } else {
                    gc.strokeOval(x * 300 + WIDTH / 2, -y * 300 + HEIGHT / 2, 1, 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.ifmo.machinelearning.homework3;

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
public class Visualization extends Application {

    private static final String TITLE = "SVM visualization";

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
        primaryStage.show();

        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("./HomeWorks/res/homework3/LinearDataset"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, " ");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }
        drawValues(canvas.getGraphicsContext2D(), sample);
    }

    private void drawAxis(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
        gc.strokeLine(HEIGHT / 2, 0, HEIGHT / 2, WIDTH);
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

    private void drawLine(GraphicsContext gc, double k, double b) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH);
//        gc.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

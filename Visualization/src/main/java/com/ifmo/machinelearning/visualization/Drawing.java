package com.ifmo.machinelearning.visualization;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Whiplash on 12.11.2014.
 */
public abstract class Drawing extends Application {

    private static final int LINE_WIDTH = 4;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private double scale;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(getTitle());
        primaryStage.setResizable(false);
        Group root = new Group();
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        scale = getScale();
        initialize();
        primaryStage.show();
    }

    protected abstract void initialize();

    protected abstract String getTitle();

    protected abstract double getScale();

    public void drawAxis() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.strokeLine(0, WIDTH / 2, HEIGHT, WIDTH / 2);
        context.strokeLine(HEIGHT / 2, 0, HEIGHT / 2, WIDTH);
    }

    public void drawLine(double k, double b) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setLineWidth(2);
        context.strokeLine(0, -((double) -(WIDTH / 2) / scale * k + b) * scale + HEIGHT / 2, WIDTH, -((double) (WIDTH / 2) / scale * k + b) * scale + HEIGHT / 2);
    }

    public void drawValues(List<ClassifiedInstance> sample) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.RED);
        context.setStroke(Color.BLUE);
        context.setLineWidth(LINE_WIDTH);
        for (ClassifiedInstance point : sample) {
            if (point.getClassId() == 1) {
                context.fillOval(point.getAttributeValue(0) * scale + WIDTH / 2, -point.getAttributeValue(1) * scale + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
            } else {
                context.strokeOval(point.getAttributeValue(0) * scale + WIDTH / 2, -point.getAttributeValue(1) * scale + HEIGHT / 2, 1, 1);
            }
        }
    }

    public void drawValues(AbstractInstanceClassifier classifier, String... attributeNames) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.BURLYWOOD);
        context.setStroke(Color.WHITE);
        context.setLineWidth(LINE_WIDTH);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new ClassifiedInstance(attributeNames, new double[]{x, y}, 2)) == 1) {
                    context.fillOval(x * scale + WIDTH / 2, -y * scale + HEIGHT / 2, LINE_WIDTH + 1, LINE_WIDTH + 1);
                } else {
                    context.strokeOval(x * scale + WIDTH / 2, -y * scale + HEIGHT / 2, 1, 1);
                }
            }
        }
    }

}

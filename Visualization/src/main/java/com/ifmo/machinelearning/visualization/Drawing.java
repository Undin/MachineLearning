package com.ifmo.machinelearning.visualization;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.ClassifiedInstanceDefaultImpl;
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

    protected final int width = getWidth();
    protected final int height = getHeight();

    protected Canvas canvas;
    protected Scene scene;
    protected double scale;
    protected int lineWidth = 4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(getTitle());
        primaryStage.setResizable(false);
        Group root = new Group();
        canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        scale = getScale();
        initialize();
        primaryStage.show();
    }

    protected abstract void initialize();

    protected abstract String getTitle();

    protected abstract double getScale();

    protected abstract int getHeight();

    protected abstract int getWidth();

    public void setLineWidth(int width) {
        lineWidth = width;
    }

    public void drawAxis() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.strokeLine(0, width / 2, height, width / 2);
        context.strokeLine(height / 2, 0, height / 2, width);
    }

    public void drawLine(double k, double b) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setLineWidth(2);
        context.strokeLine(0, -((double) -(width / 2) / scale * k + b) * scale + height / 2, width, -((double) (width / 2) / scale * k + b) * scale + height / 2);
    }

    public void drawValues(List<ClassifiedInstance> sample) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.RED);
        context.setStroke(Color.BLUE);
        context.setLineWidth(lineWidth);
        for (ClassifiedInstance point : sample) {
            if (point.getClassId() == 1) {
                context.fillOval(point.getAttributeValue(0) * scale + width / 2, -point.getAttributeValue(1) * scale + height / 2, lineWidth + 1, lineWidth + 1);
            } else {
                context.strokeOval(point.getAttributeValue(0) * scale + width / 2, -point.getAttributeValue(1) * scale + height / 2, 1, 1);
            }
        }
    }

    public void drawValues(AbstractInstanceClassifier classifier, String... attributeNames) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.BURLYWOOD);
        context.setStroke(Color.WHITE);
        context.setLineWidth(lineWidth);
        for (double x = -1.5; x < 1.5; x += 0.01) {
            for (double y = -1.5; y < 1.5; y += 0.01) {
                if (classifier.getSupposedClassId(new ClassifiedInstanceDefaultImpl(attributeNames, new double[]{x, y}, 2)) == 1) {
                    context.fillOval(x * scale + width / 2, -y * scale + height / 2, lineWidth + 1, lineWidth + 1);
                } else {
                    context.strokeOval(x * scale + width / 2, -y * scale + height / 2, 1, 1);
                }
            }
        }
    }

}

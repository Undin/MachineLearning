package com.ifmo.machinelearning.homework7;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Whiplash on 08.12.2014.
 */
public class DrawNumber extends Application {
    private byte[][] bitmap = new byte[28][28];

    private final static int SCALE = 10;
    private Canvas canvas;
    private NumberRecognitionNeuralNet net;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        Group root = new Group();
        canvas = new Canvas(28 * SCALE, 28 * SCALE);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseEntered(mouseHandler);
        scene.setOnMouseExited(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);

        net = NumberRecognitionNeuralNet.fromDump("HomeWorks/res/homework7/08-12-2014_00.03.17.523_committee_0_500_0.05_0.9275.txt");

        clear();
        draw();
        primaryStage.show();
    }

    EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
        int x = Math.min((int) (mouseEvent.getX() / SCALE), 27);
        int y = Math.min((int) (mouseEvent.getY() / SCALE), 27);
        if (x >= 0 && y >= 0) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                clear();
                bitmap[x][y] = -1;
                draw();
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                bitmap[x][y] = -1;
                draw();
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                byte[] b = new byte[28 * 28];
                for (int i = 0; i < bitmap.length; i++) {
                    for (int j = 0; j < bitmap[0].length; j++) {
                        b[i + j * 28] = bitmap[i][j];
                    }
                }
                NumberImageInstance instance = new NumberImageInstance(b, (byte) 0);
                System.out.println(net.getNumber(instance));
            }
        }
    };

    public void clear() {
        for (int i = 0; i < bitmap.length; i++) {
            for (int j = 0; j < bitmap[0].length; j++) {
                bitmap[i][j] = 0;
            }
        }
    }

    public void draw() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int i = 0; i < bitmap.length; i++) {
            for (int j = 0; j < bitmap[0].length; j++) {
                double channel = (bitmap[i][j] & 0xFF) / 255.;
                context.setFill(new Color(channel, channel, channel, 1));
                context.fillRect(i * SCALE, j * SCALE, SCALE, SCALE);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

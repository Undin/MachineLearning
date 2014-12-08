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
    private double[][] bitmap = new double[28 * SCALE][28 * SCALE];

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
        int x = Math.min((int) (mouseEvent.getX()), 28 * SCALE - 1);
        int y = Math.min((int) (mouseEvent.getY()), 28 * SCALE - 1);
        if (x >= 0 && y >= 0) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                clear();
                drawPixel(x, y);
                draw();
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                drawPixel(x, y);
                draw();
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                byte[] b = new byte[28 * 28];
                for (int i = 0; i < 28; i++) {
                    for (int j = 0; j < 28; j++) {
                        double channel = 0.;
                        for (int di = 0; di < SCALE; di++) {
                            for (int dj = 0; dj < SCALE; dj++) {
                                channel += bitmap[i * SCALE + di][j * SCALE + dj];
                            }
                        }
                        channel /= SCALE * SCALE;
                        b[i + j * 28] = (byte) (channel * 255);
                    }
                }

                /*for (int i = 0; i < 28; i++) {
                    for (int j = 0; j < 28; j++) {
                        System.out.print(b[i * 28 + j] + " ");
                    }
                    System.out.println();
                }*/
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

    public void drawPixel(int x, int y) {
        for (int i = x; i < Math.min(x + SCALE * 2, 28 * SCALE); i++) {
            for (int j = y; j < Math.min(y + SCALE * 2, 28 * SCALE); j++) {
                bitmap[i][j] = 1;
            }
        }
    }

    public void draw() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                double channel = 0;
                for (int di = 0; di < SCALE; di++) {
                    for (int dj = 0; dj < SCALE; dj++) {
                        channel += bitmap[i * SCALE + di][j * SCALE + dj];
                    }
                }
                channel /= SCALE * SCALE;
                context.setFill(new Color(channel, channel, channel, 1));
                context.fillRect(i * SCALE, j * SCALE, SCALE, SCALE);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

package com.ifmo.machinelearning.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Whiplash on 01.12.2014.
 */
public abstract class DrawingNumber extends Drawing {

    public void drawNumber(double[] pixels) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                double channel = pixels[i * 28 + j];
                context.setFill(new Color(channel, channel, channel, 1));
                context.fillRect(j * scale, i * scale, lineWidth, lineWidth);
            }
        }
    }

}

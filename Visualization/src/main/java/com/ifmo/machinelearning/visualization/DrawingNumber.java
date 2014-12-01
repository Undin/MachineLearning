package com.ifmo.machinelearning.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Whiplash on 01.12.2014.
 */
public abstract class DrawingNumber extends Drawing {

    public void drawNumber(byte[][] pixels) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                double channel = (pixels[i][j] & 0xFF) / 255.;
                context.setFill(new Color(channel, channel, channel, 1));
                context.fillRect(j * scale, i * scale, lineWidth, lineWidth);
            }
        }
    }

}

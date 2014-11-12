package com.ifmo.machinelearning.visualization;

import org.math.plot.Plot3DPanel;

import javax.swing.*;

/**
 * Created by Whiplash on 13.11.2014.
 */
public class Plot3DBuilder {

    private Plot3DPanel plot;

    public Plot3DBuilder() {
        this.plot = new Plot3DPanel("SOUTH");
    }

    public void addGridPlot(String plotName, double[] x, double[] y, double[][] z) {
        plot.addGridPlot(plotName, x, y, z);
    }

    public void addScatterPlot(String plotName, double[] x, double[] y, double[] z) {
        plot.addScatterPlot(plotName, x, y, z);
    }

    public void show() {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }

}

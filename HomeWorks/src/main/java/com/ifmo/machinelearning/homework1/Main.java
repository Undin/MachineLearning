package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.graphics.Plot2DBuilder;
import com.ifmo.machinelearning.test.Statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class Main {

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 20;

    public static void main(String[] args) throws IOException, InterruptedException {
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

        Plot2DBuilder builder = new Plot2DBuilder("k", "F-measure");

        Collections.shuffle(sample);
        List<Point> first = new ArrayList<>(sample.subList(0, sample.size() / 5));
        List<Point> second = new ArrayList<>(sample.subList(sample.size() / 5, sample.size()));

        KNNTestMachine testMachine = new KNNTestMachine(second, true);
        {
            int n = (second.size() / FOLD_NUMBER) * (FOLD_NUMBER - 1);
            double[] xAxis = new double[n - 1];
            double[] yAxis = new double[n - 1];
            for (int k = 1; k < n; k++) {
                testMachine.setK(k);
                Statistics statistics = testMachine.crossValidationTest(FOLD_NUMBER, ROUNDS);
                xAxis[k - 1] = k;
                yAxis[k - 1] = statistics.getFMeasure();
            }
            builder.addPlot("Training", xAxis, yAxis);
        }

        {
            int n = 40;
            double[] xAxis = new double[n - 1];
            double[] yAxis = new double[n - 1];
            for (int k = 1; k < n; k++) {
                testMachine.setK(k);
                Statistics statistics = testMachine.test(first);
                xAxis[k - 1] = k;
                yAxis[k - 1] = statistics.getFMeasure();
            }
            builder.addPlot("Test", xAxis, yAxis);
        }

        builder.show();
    }

}

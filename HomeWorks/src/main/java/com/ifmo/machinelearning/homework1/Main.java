package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.graphics.Plot2DBuilder;
import com.ifmo.machinelearning.library.ClassifiedInstance;
import com.ifmo.machinelearning.library.InstanceCreator;
import com.ifmo.machinelearning.library.test.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class Main {

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 20;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");

        Plot2DBuilder builder = new Plot2DBuilder("k", "F-measure");

        Collections.shuffle(sample);
        List<ClassifiedInstance> first = new ArrayList<>(sample.subList(0, sample.size() / 5));
        List<ClassifiedInstance> second = new ArrayList<>(sample.subList(sample.size() / 5, sample.size()));

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

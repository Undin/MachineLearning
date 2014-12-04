package com.ifmo.machinelearning.homework4;

import com.ifmo.machinelearning.library.core.Instance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.core.InstanceDefaultImpl;
import com.ifmo.machinelearning.library.regression.LinearRegression;
import com.ifmo.machinelearning.visualization.Plot3DBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Created by warrior on 13.11.14
 */
public class Main {

    public static void main(String[] args) {
        List<Instance> instances = InstanceCreator.instancesFromFile("./HomeWorks/res/homework4/prices.txt");
        int size = instances.size();
        Collections.shuffle(instances);
        List<Instance> train = instances.subList(0, (int) (size * 0.8));
        List<Instance> test = instances.subList((int) (size * 0.8), size);

        LinearRegression regression = new LinearRegression(train, 2);
        regression.setPenalty(1);
        regression.train();
        double rmse = 0;
        for (Instance instance : test) {
            double value = regression.getSupposedValue(instance);
            System.out.println(String.format("real %f - res %f", instance.getAttributeValue(2), value));
            rmse += Math.pow(instance.getAttributeValue(2) - value, 2);
        }
        rmse = Math.sqrt(rmse / test.size());

        double[] x = {1, 5};
        double[] y = {800, 4600};
        double[][] z = new double[y.length][x.length];
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < x.length; j++) {
                z[i][j] = regression.getSupposedValue(new InstanceDefaultImpl(new String[]{"square", "flat_number", "price"}, new double[]{y[i], x[j], 0}));
            }
        }

        double[] xo = new double[instances.size()];
        double[] yo = new double[instances.size()];
        double[] zo = new double[instances.size()];
        for (int i = 0; i < instances.size(); i++) {
            Instance instance = instances.get(i);
            yo[i] = instance.getAttributeValue(0);
            xo[i] = instance.getAttributeValue(1);
            zo[i] = instance.getAttributeValue(2);
        }
        for (double coef : regression.getCoefficients()) {
            System.out.println("> " + coef);
        }

        System.out.println("RMSE " + rmse);

        Plot3DBuilder builder = new Plot3DBuilder();
        builder.addGridPlot("regression", x, y, z);
        builder.addScatterPlot("sample", xo, yo, zo);
        builder.show();
    }
}

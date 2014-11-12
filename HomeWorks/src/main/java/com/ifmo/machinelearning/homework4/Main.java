package com.ifmo.machinelearning.homework4;

import com.ifmo.machinelearning.library.core.Instance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.regression.LinearRegression;

import java.util.Collections;
import java.util.List;

/**
 * Created by warrior on 13.11.14.
 */
public class Main {

    public static void main(String[] args) {
        List<Instance> instances = InstanceCreator.instancesFromFile("./HomeWorks/res/homework4/prices.txt");
        int size = instances.size();
        Collections.shuffle(instances);
        List<Instance> train = instances.subList(0, (int) (size * 0.8));
        List<Instance> test = instances.subList((int) (size * 0.8), size);
        LinearRegression regression = new LinearRegression(train, 2);
        regression.train();
        double rmse = 0;
        for (Instance instance : test) {
            double value = regression.getSupposedValue(instance);
            System.out.println("real = " + instance.getAttributeValue(2) + ", res = " + value);
            rmse += Math.pow(instance.getAttributeValue(2) - value, 2);
        }
        rmse = Math.sqrt(rmse / test.size());
        System.out.println(rmse);
    }
}

package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.*;
import com.ifmo.machinelearning.test.TestMachine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNNTestMachine extends TestMachine<Point> {

    private int k = 1;

    public KNNTestMachine(List<Point> dataSet) {
        super(dataSet);
    }

    public KNNTestMachine(List<Point> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    protected ExecutorService generateExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    @Override
    protected Classifier<Point> createClassifier(List<Point> dataSet) {
        Distance<Point, Double> distance = EuclideanDistance.getInstance();
        return new KNN<>(dataSet, distance, KernelWeight.getInstance(GaussKernel.getInstance()), k);
    }

}

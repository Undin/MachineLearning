package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.knn.DistanceWeight;
import com.ifmo.machinelearning.library.knn.KNN;
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
        return new KNN<>(dataSet, ManhattanDistance.getInstance(), DistanceWeight.getInstance(), k);
    }

}

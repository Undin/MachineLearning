package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.Distance;
import com.ifmo.machinelearning.library.KNN;
import com.ifmo.machinelearning.library.LinearWeight;
import com.ifmo.machinelearning.test.TestMachine;

import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNNTestMachine extends TestMachine<Point> {

    private int k = 1;

    public KNNTestMachine(TestType testType) {
        super(testType);
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    protected Classifier<Point> createClassifier(List<Point> dataSet) {
        Distance<Point, Double> distance = EuclideanDistance.getInstance();
        return new KNN<>(dataSet, distance, new LinearWeight<>(distance), k);
    }

}

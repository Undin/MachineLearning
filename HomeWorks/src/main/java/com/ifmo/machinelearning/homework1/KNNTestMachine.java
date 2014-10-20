package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.classifiers.knn.DistanceWeight;
import com.ifmo.machinelearning.library.classifiers.knn.KNN;
import com.ifmo.machinelearning.library.classifiers.knn.ManhattanDistance;
import com.ifmo.machinelearning.library.test.TestMachine;

import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNNTestMachine extends TestMachine {

    private int k = 1;

    public KNNTestMachine(List<ClassifiedInstance> dataSet) {
        super(dataSet);
    }

    public KNNTestMachine(List<ClassifiedInstance> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    protected AbstractInstanceClassifier createClassifier(List<ClassifiedInstance> dataSet) {
        return new KNN(dataSet, new ManhattanDistance(), new DistanceWeight(), k);
    }
}

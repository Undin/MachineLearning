package com.ifmo.machinelearning;

import com.ifmo.machinelearning.test.ClassifiedData;
import com.ifmo.machinelearning.test.Classifier;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNN<T extends ClassifiedData> implements Classifier<T> {

    private List<T> trainingSample;
    private Distance<T, Double> distanceFunction;
    private Weight<T, Double> weightFunction;

    private Comparator<T> comparator = new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
            return Integer.compare(o1.getClassId(), o2.getClassId());
        }
    };

    public KNN(List<T> trainingSample, Distance<T, Double> distanceFunction, Weight<T, Double> weightFunction) {
        this.trainingSample = trainingSample;
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
    }

    @Override
    public Classifier<T> training() {
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        double[] weights = new double[t.getClassNumber()];
        return 0;
    }
}

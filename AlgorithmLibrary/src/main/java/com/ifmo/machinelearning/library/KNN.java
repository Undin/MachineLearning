package com.ifmo.machinelearning.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNN<T extends ClassifiedData> implements Classifier<T> {

    private List<T> trainingSample;
    private Distance<T, Double> distanceFunction;
    private Weight<T, Double> weightFunction;
    private int k;

    public KNN(List<T> sample, Distance<T, Double> distanceFunction, Weight<T, Double> weightFunction, int k) {
        this.trainingSample = sample;
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
        this.k = Math.min(k, sample.size());
    }

    @Override
    public Classifier<T> training() {
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        Integer[] ids = new Integer[trainingSample.size()];
        double[] distances = new double[trainingSample.size()];
        Arrays.setAll(ids, i -> i);
        Arrays.setAll(distances, i -> distanceFunction.distance(t, trainingSample.get(i)));
        Arrays.sort(ids, (Integer o1, Integer o2) -> Double.compare(distances[o1], distances[o2]));

        double[] weights = new double[t.getClassNumber()];
        for (int i = 0; i < k; i++) {
            weights[trainingSample.get(ids[i]).getClassId()] += weightFunction.weight(t, trainingSample.get(i));
        }

        double maxWeight = 0;
        int maxId = -1;
        for (int i = 0; i < weights.length; i++) {
            if (maxWeight < weights[i]) {
                maxWeight = weights[i];
                maxId = i;
            }
        }

        return maxId;
    }
}

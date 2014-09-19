package com.ifmo.machinelearning;

import com.ifmo.machinelearning.test.ClassifiedData;
import com.ifmo.machinelearning.test.Classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class KNN<T extends ClassifiedData> implements Classifier<T> {

    private List<T> trainingSample;
    private Distance<T, Double> distanceFunction;
    private Weight<T, Double> weightFunction;
    private int k;

    public KNN(List<T> trainingSample, Distance<T, Double> distanceFunction, Weight<T, Double> weightFunction, int k) {
        this.trainingSample = trainingSample;
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
        this.k = k;
    }

    @Override
    public Classifier<T> training() {
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        double[] distances = new double[trainingSample.size()];
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            ids.add(i);
            distances[i] = distanceFunction.distance(t, trainingSample.get(i));
        }
        Collections.sort(ids, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Double.compare(distances[o1], distances[o2]);
            }
        });

        double[] weights = new double[t.getClassNumber()];
        for (int i = 0; i < k; i++) {
            weights[trainingSample.get(ids.get(i)).getClassId()] += weightFunction.weight(t, trainingSample.get(i));
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

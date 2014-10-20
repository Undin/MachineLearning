package com.ifmo.machinelearning.library.classifiers.knn;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class DistanceWeight implements Weight {

    @Override
    public double weight(double distance) {
        return 1 / distance;
    }
}

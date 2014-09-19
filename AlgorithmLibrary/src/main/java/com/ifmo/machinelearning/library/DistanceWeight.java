package com.ifmo.machinelearning.library;

/**
 * Created by warrior on 19.09.14.
 */
public abstract class DistanceWeight<T> implements Weight<T, Double> {

    private Distance<T, Double> distance;

    public DistanceWeight(Distance<T, Double> distance) {
        this.distance = distance;
    }

    protected Double distance(T first, T second) {
        return distance.distance(first, second);
    }
}

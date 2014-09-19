package com.ifmo.machinelearning.library;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class LinearWeight<T> extends DistanceWeight<T> {

    public LinearWeight(Distance<T, Double> distance) {
        super(distance);
    }

    @Override
    public Double weight(T first, T second) {
        return 1 / distance(first, second);
    }

}

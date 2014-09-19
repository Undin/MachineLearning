package com.ifmo.machinelearning;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class LinearWeight<T> implements Weight<T, Double> {

    private Distance<T, Double> distanceFunction;

    public LinearWeight(Distance<T, Double> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public Double weight(T first, T second) {
        return 1 / distanceFunction.distance(first, second);
    }

}

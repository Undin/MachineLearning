package com.ifmo.machinelearning;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class PointWeight<Point, Double> implements Weight<Point, Double> {

    private Distance<Point, Double> distanceFunction;

    public PointWeight(Distance<Point, Double> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public Double weight(Point first, Point second) {
        return distanceFunction.distance(first, second);
    }

}

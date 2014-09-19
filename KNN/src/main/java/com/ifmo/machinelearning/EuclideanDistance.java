package com.ifmo.machinelearning;

/**
 * Created by warrior on 19.09.14.
 */
public class EuclideanDistance implements Distance<Point, Double> {
    @Override
    public Double distanceTo(Point first, Point second) {
        return StrictMath.sqrt(sqr(first.getX() - second.getX() + sqr(first.getY() - second.getY())));
    }

    private double sqr(double x) {
        return x * x;
    }
}

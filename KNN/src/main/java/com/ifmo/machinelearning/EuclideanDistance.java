package com.ifmo.machinelearning;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by warrior on 19.09.14.
 */
public class EuclideanDistance implements Distance<Point, Double> {

    @Override
    public Double distance(Point first, Point second) {
        return sqrt(pow(first.getX() - second.getX(), 2) + pow(first.getY() - second.getY(), 2));
    }

}

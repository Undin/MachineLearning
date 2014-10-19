package com.ifmo.machinelearning;

import com.ifmo.machinelearning.library.knn.Distance;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by warrior on 19.09.14.
 */
public class EuclideanDistance implements Distance<Point, Double> {

    private static EuclideanDistance instance = null;

    public static EuclideanDistance getInstance() {
        if (instance == null) {
            instance = new EuclideanDistance();
        }
        return instance;
    }

    private EuclideanDistance() {
    }

    @Override
    public Double distance(Point first, Point second) {
        return sqrt(pow(first.getX() - second.getX(), 2) + pow(first.getY() - second.getY(), 2));
    }

}

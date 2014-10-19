package com.ifmo.machinelearning;

import com.ifmo.machinelearning.library.knn.Distance;

import static java.lang.StrictMath.abs;

/**
 * Created by warrior on 19.09.14.
 */
public class ManhattanDistance implements Distance<Point, Double> {

    private static ManhattanDistance instance = null;

    public static ManhattanDistance getInstance() {
        if (instance == null) {
            instance = new ManhattanDistance();
        }
        return instance;
    }

    private ManhattanDistance() {
    }


    @Override
    public Double distance(Point first, Point second) {
        return abs(first.getX() - second.getX()) + abs(first.getY() - second.getY());
    }

}

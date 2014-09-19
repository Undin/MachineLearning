package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.Distance;

import static java.lang.StrictMath.abs;

/**
 * Created by warrior on 19.09.14.
 */
public class ManhattanDistance implements Distance<Point, Double> {

    @Override
    public Double distance(Point first, Point second) {
        return abs(first.getX() - second.getX()) + abs(first.getY() - second.getY());
    }

}

package com.ifmo.machinelearning;

/**
 * Created by warrior on 19.09.14.
 */
public class ManhattanDistance implements Distance<Point, Double> {
    @Override
    public Double distanceTo(Point first, Point second) {
        return StrictMath.abs(first.getX() - second.getX()) + StrictMath.abs(first.getY() - second.getY());
    }
}

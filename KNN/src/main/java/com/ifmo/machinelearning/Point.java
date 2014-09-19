package com.ifmo.machinelearning;

/**
 * Created by warrior on 19.09.14.
 */
public class Point {

    public static enum PointClass {
        FIRST,
        SECOND,
        UNDEFINED
    }

    private double x;
    private double y;
    private PointClass pointClass;

    public Point(double x, double y, PointClass pointClass) {
        this.x = x;
        this.y = y;
        this.pointClass = pointClass;
    }

    public Point(double x, double y) {
        this(x, y, PointClass.UNDEFINED);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public PointClass getPointClass() {
        return pointClass;
    }
}

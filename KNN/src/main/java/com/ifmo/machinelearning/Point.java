package com.ifmo.machinelearning;

import com.ifmo.machinelearning.library.ClassifiedData;

/**
 * Created by warrior on 19.09.14.
 */
public class Point implements ClassifiedData {

    private double x;
    private double y;
    private int classId;

    public Point(double x, double y, int classId) {
        this.x = x;
        this.y = y;
        this.classId = classId;
    }

    public Point(double x, double y) {
        this(x, y, -1);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public int getClassNumber() {
        return 2;
    }
}

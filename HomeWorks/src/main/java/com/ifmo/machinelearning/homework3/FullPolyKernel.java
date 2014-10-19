package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

import static java.lang.StrictMath.pow;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class FullPolyKernel implements Kernel2<Point> {

    private static FullPolyKernel instance;

    private int power;

    public static FullPolyKernel getInstance(int power) {
        if (instance == null) {
            instance = new FullPolyKernel(power);
        }
        return instance;
    }

    private FullPolyKernel(int power) {
        this.power = power;
    }

    @Override
    public double eval(Point first, Point second) {
        return pow(1 + first.getX() * second.getX() + first.getY() * second.getY(), power);
    }

}

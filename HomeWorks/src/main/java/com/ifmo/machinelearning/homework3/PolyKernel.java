package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

import static java.lang.StrictMath.pow;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class PolyKernel implements Kernel2<Point> {

    private static PolyKernel instance;

    private int power;

    public static PolyKernel getInstance(int power) {
        if (instance == null) {
            instance = new PolyKernel(power);
        }
        return instance;
    }

    private PolyKernel(int power) {
        this.power = power;
    }

    @Override
    public double eval(Point first, Point second) {
        return pow(first.getX() * second.getX() + first.getY() * second.getY(), power);
    }

}

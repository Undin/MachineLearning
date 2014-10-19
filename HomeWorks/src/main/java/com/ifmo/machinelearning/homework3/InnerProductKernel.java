package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class InnerProductKernel implements Kernel2<Point> {

    private static InnerProductKernel instance;

    public static InnerProductKernel getInstance() {
        if (instance == null) {
            instance = new InnerProductKernel();
        }
        return instance;
    }

    private InnerProductKernel() {
    }

    @Override
    public double eval(Point first, Point second) {
        return first.getX() * second.getX() + first.getY() * second.getY();
    }

}

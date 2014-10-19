package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class MultiKernel implements Kernel2<Point> {

    private static MultiKernel instance;

    private Kernel2<Point> firstKernel;
    private Kernel2<Point> secondKernel;

    public static MultiKernel getInstance(Kernel2<Point> firstKernel, Kernel2<Point> secondKernel) {
        if (instance == null) {
            instance = new MultiKernel(firstKernel, secondKernel);
        }
        return instance;
    }

    private MultiKernel(Kernel2<Point> firstKernel, Kernel2<Point> secondKernel) {
        this.firstKernel = firstKernel;
        this.secondKernel = secondKernel;
    }

    @Override
    public double eval(Point first, Point second) {
        return firstKernel.eval(first, second) * secondKernel.eval(first, second);
    }

}

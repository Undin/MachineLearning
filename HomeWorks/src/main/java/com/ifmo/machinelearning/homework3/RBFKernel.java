package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.EuclideanDistance;
import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

import static java.lang.StrictMath.exp;
import static java.lang.StrictMath.pow;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class RBFKernel implements Kernel2<Point> {

    private static RBFKernel instance;

    private double gamma;

    public static RBFKernel getInstance(double gamma) {
        if (instance == null) {
            instance = new RBFKernel(gamma);
        }
        return instance;
    }

    private RBFKernel(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double eval(Point first, Point second) {
        return exp(pow(-gamma * EuclideanDistance.getInstance().distance(first, second), 2));
    }

}

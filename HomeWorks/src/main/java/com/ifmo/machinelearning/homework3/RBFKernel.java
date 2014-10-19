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

    private double gamma;

    public RBFKernel(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double eval(Point first, Point second) {
        return exp(-gamma * pow(EuclideanDistance.getInstance().distance(first, second), 2));
    }

}

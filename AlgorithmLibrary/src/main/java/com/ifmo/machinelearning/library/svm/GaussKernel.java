package com.ifmo.machinelearning.library.svm;

import com.ifmo.machinelearning.library.Instance;
import com.ifmo.machinelearning.library.knn.EuclideanDistance;

import static java.lang.StrictMath.exp;
import static java.lang.StrictMath.pow;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class GaussKernel implements Kernel {

    private EuclideanDistance distance = new EuclideanDistance();
    private double gamma;

    public GaussKernel(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double eval(Instance first, Instance second) {
        return exp(-gamma * pow(distance.distance(first, second), 2));
    }
}

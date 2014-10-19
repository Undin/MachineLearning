package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.Kernel2;
import com.ifmo.machinelearning.library.svm.SVMClassifier;

import java.util.List;

/**
 * Created by warrior on 19.10.14.
 */
public class PointSVMClassifier extends SVMClassifier<Point> {

    private double[] w = new double[2];

    public PointSVMClassifier(List<Point> data, Kernel2<Point> kernelFunction, double c) {
        super(data, kernelFunction, c);
    }

    @Override
    public Classifier<Point> training() {
        super.training();
        for (int i = 0; i < getData().size(); i++) {
            w[0] += alphas[i] * modifiedClassIds[i] * getData().get(i).getX();
            w[1] += alphas[i] * modifiedClassIds[i] * getData().get(i).getY();
        }
        return this;
    }

    public double getK() {
        return -w[0] / w[1];
    }

    public double getB() {
        return -b / w[1];
    }
}

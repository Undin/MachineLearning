package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.ClassifiedInstance;
import com.ifmo.machinelearning.library.svm.Kernel;
import com.ifmo.machinelearning.library.svm.SVMClassifier;

import java.util.List;

/**
 * Created by warrior on 19.10.14.
 */
public class PointSVMClassifier extends SVMClassifier {

    private static final int ATTRIBUTE_NUMBER = 2;

    private double[] w = new double[2];

    public PointSVMClassifier(List<ClassifiedInstance> data, Kernel kernelFunction, double c) {
        super(data, kernelFunction, c);
    }

    @Override
    protected void trainingInternal() {
        super.trainingInternal();
        for (int j = 0; j < ATTRIBUTE_NUMBER; j++) {
            for (int i = 0; i < size; i++) {
                w[j] += alphas[i] * modifiedClassIds[i] * get(i).getAttributeValue(j);
            }
        }
    }

    public double getK() {
        return -w[0] / w[1];
    }

    public double getB() {
        return -b / w[1];
    }
}

package com.ifmo.machinelearning.library.classifiers.svm;

import com.ifmo.machinelearning.library.core.Instance;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class InnerProductKernel implements Kernel {

    @Override
    public double eval(Instance first, Instance second) {
        double result = 0;
        for (int i = 0; i < first.getAttributeNumber(); i++) {
            result += first.getAttributeValue(i) * second.getAttributeValue(i);
        }
        return result;
    }
}

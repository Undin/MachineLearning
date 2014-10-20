package com.ifmo.machinelearning.library.classifiers.knn;

import com.ifmo.machinelearning.library.core.Instance;

/**
 * Created by warrior on 19.09.14.
 */
public class ManhattanDistance implements Distance {

    @Override
    public double distance(Instance first, Instance second) {
        double result = 0;
        for (int i = 0; i < first.getAttributeNumber(); i++) {
            result += StrictMath.abs(first.getAttributeValue(i) - second.getAttributeValue(i));
        }
        return result;
    }
}

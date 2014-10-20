package com.ifmo.machinelearning.library.knn;

import com.ifmo.machinelearning.library.Instance;

/**
 * Created by warrior on 19.09.14.
 */
public class EuclideanDistance implements Distance {

    @Override
    public double distance(Instance first, Instance second) {
        double result = 0;
        for (int i = 0; i < first.getAttributeNumber(); i++) {
            result += StrictMath.pow(first.getAttributeValue(i) - second.getAttributeValue(i), 2);
        }
        return StrictMath.sqrt(result);
    }
}

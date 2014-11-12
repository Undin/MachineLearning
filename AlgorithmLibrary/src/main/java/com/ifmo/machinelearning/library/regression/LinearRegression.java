package com.ifmo.machinelearning.library.regression;

import com.ifmo.machinelearning.library.core.Instance;
import com.ifmo.machinelearning.library.core.TrainingAlgorithm;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;

import java.util.List;

/**
 * Created by warrior on 12.11.14.
 */
public class LinearRegression implements TrainingAlgorithm {

    private final int index;

    private List<Instance> dataSet;
    private double penalty = 0;
    private double[] coefficients;

    public LinearRegression(List<Instance> dataSet, int index) {
        this.index = index;
        this.dataSet = dataSet;
    }

    @Override
    public LinearRegression train() {
        int attributeNumber = dataSet.get(0).getAttributeNumber() - 1;
        DoubleMatrix F = new DoubleMatrix(dataSet.size(), attributeNumber);
        DoubleMatrix Y = new DoubleMatrix(dataSet.size(), 1);
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            int j = 0;
            for (int k = 0; k < instance.getAttributeNumber(); k++) {
                if (k != index) {
                    F.put(i, j, instance.getAttributeValue(k));
                    j++;
                } else {
                    Y.put(i, 0, instance.getAttributeValue(k));
                }
            }
        }
        dataSet = null;
        DoubleMatrix transposedF = F.transpose();
        DoubleMatrix tI = DoubleMatrix.eye(attributeNumber).mul(penalty);
        DoubleMatrix coef = Solve.pinv(transposedF.mmul(F).add(tI)).mmul(transposedF).mmul(Y);
        coefficients = coef.toArray();
        return this;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getSupposedValue(Instance instance) {
        if (coefficients == null) {
            throw new IllegalStateException("algorithm must be trained at first");
        }
        double value = 0;
        for (int i = 0; i < instance.getAttributeNumber(); i++) {
            if (i != index) {
                value += coefficients[i] * instance.getAttributeValue(i);
            }
        }
        return value;
    }

    public double[] getCoefficients() {
        return coefficients;
    }
}

package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 05.12.14.
 */
public class LogisticFunction implements ActivationFunction {
    @Override
    public double calculate(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    @Override
    public double calculateFirstDerivative(double value) {
        double f = calculate(value);
        return f * (1 - f);
    }
}

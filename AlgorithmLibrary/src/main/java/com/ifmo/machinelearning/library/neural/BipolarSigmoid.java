package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 07.12.14.
 */
public class BipolarSigmoid implements ActivationFunction {
    @Override
    public double calculate(double value) {
        return 2 / (1 + Math.exp(-value)) - 1;
    }

    @Override
    public double calculateFirstDerivative(double value) {
        double f = calculate(value);
        return 0.5 * (1 + f) * (1 - f);
    }
}

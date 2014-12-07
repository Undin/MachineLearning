package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 03.12.14.
 */
public class Id implements ActivationFunction {
    @Override
    public double calculate(double value) {
        return value;
    }

    @Override
    public double calculateFirstDerivative(double value) {
        return 1;
    }
}

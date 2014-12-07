package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 03.12.14.
 */
public interface ActivationFunction {

    public double calculate(double value);

    public double calculateFirstDerivative(double value);
}

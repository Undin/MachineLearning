package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 03.12.14.
 */
public class NeuralLink {

    private double alpha = -1;
    private double weight;
    private double inputSignal;
    private double error;

    public NeuralLink(double weight, double alpha) {
        this.weight = weight;
        this.alpha = alpha;
    }

    public NeuralLink(double weight) {
        this.weight = weight;
    }

    public void setInputSignal(double inputSignal) {
        this.inputSignal = inputSignal;
    }

    public double getOutputSignal() {
        return weight * inputSignal;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getError() {
        if (alpha == -1) {
            throw new UnsupportedOperationException();
        }
        double outputError = weight * error;
        weight += alpha * error * inputSignal;
        return outputError;
    }

    public double getWeight() {
        return weight;
    }
}

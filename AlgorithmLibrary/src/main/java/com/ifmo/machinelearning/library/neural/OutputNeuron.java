package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 04.12.14.
 */
public class OutputNeuron extends Neuron {

    private double realValue;

    public OutputNeuron(ActivationFunction activationFunction) {
        super(activationFunction);
    }

    public void setRealValue(double realValue) {
        this.realValue = realValue;
    }

    @Override
    protected double computeError() {
        return realValue - outputSignal;
    }


}

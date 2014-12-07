package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 04.12.14.
 */
public class InputNeuron extends ConstNeuron {

    public void setInputSignal(double inputSignal) {
        this.inputSignal = inputSignal;
    }

    @Override
    protected double computeInputSignal() {
        return inputSignal;
    }
}

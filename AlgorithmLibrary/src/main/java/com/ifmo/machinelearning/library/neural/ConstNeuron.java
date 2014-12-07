package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 03.12.14.
 */
public class ConstNeuron extends Neuron {

    private static final ActivationFunction ID = new Id();

    public ConstNeuron() {
        super(ID);
    }

    @Override
    protected double computeInputSignal() {
        return 1;
    }
}

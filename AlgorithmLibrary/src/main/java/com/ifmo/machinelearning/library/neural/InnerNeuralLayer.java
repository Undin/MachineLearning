package com.ifmo.machinelearning.library.neural;

/**
 * Created by warrior on 05.12.14.
 */
public class InnerNeuralLayer extends AbstractNeuralLayer<Neuron> {

    public InnerNeuralLayer(int size, ActivationFunction function) {
        super(size, function);
    }

    @Override
    protected Neuron createNeuron() {
        return new Neuron(getActivationFunction());
    }
}

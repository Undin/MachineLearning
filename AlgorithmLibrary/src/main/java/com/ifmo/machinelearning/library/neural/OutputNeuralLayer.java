package com.ifmo.machinelearning.library.neural;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by warrior on 05.12.14.
 */
public class OutputNeuralLayer extends AbstractNeuralLayer<OutputNeuron> {

    private final List<NeuralLink> outputLinks;

    public OutputNeuralLayer(int size, ActivationFunction function) {
        super(size, function);
        outputLinks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            NeuralLink link = new OutputLink(1);
            getNeuron(i).addOutputLink(link);
            outputLinks.add(link);
        }
    }

    @Override
    protected OutputNeuron createNeuron() {
        return new OutputNeuron(getActivationFunction());
    }

    public double[] getResult() {
        double[] result = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            result[i] = outputLinks.get(i).getOutputSignal();
        }
        return result;
    }

    @Override
    public void propagateSignal() {
        super.propagateSignal();
    }

    public void setRealValues(double[] realValues) {
        if (realValues == null || realValues.length != getSize()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < realValues.length; i++) {
            getNeuron(i).setRealValue(realValues[i]);
        }
    }

    private static class OutputLink extends NeuralLink {

        public OutputLink(double weight) {
            super(weight, 0);
        }

        @Override
        public void setError(double error) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getError() {
            throw new UnsupportedOperationException();
        }
    }
}

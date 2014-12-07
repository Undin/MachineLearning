package com.ifmo.machinelearning.library.neural;

import java.util.Objects;

/**
 * Created by warrior on 04.12.14.
 */
public class InputNeuralLayer extends AbstractNeuralLayer<InputNeuron> {

    public InputNeuralLayer(int size) {
        super(size);
    }

    @Override
    protected InputNeuron createNeuron() {
        return new InputNeuron();
    }

    public void setInputSignals(double[] inputSignals) {
        Objects.requireNonNull(inputSignals);
        int signalsNum = Math.min(getSize(), inputSignals.length);
        for (int i = 0; i < signalsNum; i++) {
            getNeuron(i).setInputSignal(inputSignals[i]);
        }
    }
}

package com.ifmo.machinelearning.library.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by warrior on 03.12.14.
 */
public abstract class AbstractNeuralLayer<T extends Neuron> {

    private final List<Neuron> neurons;
    private final int size;

    private final ActivationFunction function;

    private boolean parallel = false;

    public AbstractNeuralLayer(int size, ActivationFunction function) {
        this.function = function;
        this.size = size;
        neurons = new ArrayList<>(size + 1);
        for (int i = 0; i < size; i++) {
            neurons.add(createNeuron());
        }
        neurons.add(new ConstNeuron());
    }

    public AbstractNeuralLayer(int size) {
        this(size, null);
    }

    public int getSize() {
        return size;
    }

    public void nextLayer(AbstractNeuralLayer nextLayer, double alpha, Random random) {
        for (Neuron firstNeuron : neurons) {
            for (int j = 0; j < nextLayer.getSize(); j++) {
                Neuron secondNeuron = nextLayer.getNeuron(j);
                NeuralLink link = new NeuralLink(random.nextDouble() - 0.5, alpha);
                firstNeuron.addOutputLink(link);
                secondNeuron.addInputLink(link);
            }
        }
    }

    public void nextLayer(AbstractNeuralLayer nextLayer, double[][] weights) {
        for (int i = 0; i < neurons.size(); i++) {
            Neuron firstNeuron = neurons.get(i);
            for (int j = 0; j < nextLayer.getSize(); j++) {
                Neuron secondNeuron = nextLayer.getNeuron(j);
                NeuralLink link = new NeuralLink(weights[i][j]);
                firstNeuron.addOutputLink(link);
                secondNeuron.addInputLink(link);
            }
        }
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public void propagateSignal() {
        Stream<Neuron> neuronStream = parallel ? neurons.parallelStream() : neurons.stream();
        neuronStream.forEach(Neuron::propagateSignal);
    }

    public void propagateError() {
        Stream<Neuron> neuronStream = parallel ? neurons.parallelStream() : neurons.stream();
        neuronStream.forEach(Neuron::propagateError);
    }

    protected ActivationFunction getActivationFunction() {
        return function;
    }

    @SuppressWarnings("unchecked")
    protected T getNeuron(int i) {
        if (i < 0 || i > size) {
            throw new IllegalArgumentException();
        }
        return (T) neurons.get(i);
    }

    public String dump() {
        StringBuilder builder = new StringBuilder();
        for (Neuron neuron : neurons) {
            builder.append(neuron.dump()).append('\n');
        }
        return builder.toString();
    }

    protected abstract T createNeuron();
}

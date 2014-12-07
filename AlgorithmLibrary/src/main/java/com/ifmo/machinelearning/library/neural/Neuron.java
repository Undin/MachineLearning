package com.ifmo.machinelearning.library.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by warrior on 03.12.14.
 */
public class Neuron {

    private List<NeuralLink> inputLinks = new ArrayList<>();
    private List<NeuralLink> outputLinks = new ArrayList<>();

    protected ActivationFunction activationFunction;

    protected double inputSignal;
    protected double outputSignal;

    public Neuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public void addInputLink(NeuralLink link) {
        inputLinks.add(link);
    }

    public void addOutputLink(NeuralLink link) {
        outputLinks.add(link);
    }

    public void propagateSignal() {
        inputSignal = computeInputSignal();
        outputSignal = activationFunction.calculate(inputSignal);
        outputLinks.stream().forEach(l -> l.setInputSignal(outputSignal));
    }

    protected double computeInputSignal() {
        double sum = 0;
        for (NeuralLink link : inputLinks) {
            sum += link.getOutputSignal();
        }
        return sum;
    }

    public void propagateError() {
        double error = computeError() * activationFunction.calculateFirstDerivative(inputSignal);
        for (NeuralLink link : inputLinks) {
            link.setError(error);
        }
    }

    protected double computeError() {
        double sum = 0;
        for (NeuralLink link : outputLinks) {
            sum += link.getError();
        }
        return sum;
    }

    public String dump() {
        List<String> weights = outputLinks.stream().map(link -> String.valueOf(link.getWeight())).collect(Collectors.toList());
        return String.join(" ", weights);
    }
}

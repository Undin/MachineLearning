package com.ifmo.machinelearning.library.neural;

import com.ifmo.machinelearning.library.core.Instance;
import com.ifmo.machinelearning.library.core.TrainingAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by warrior on 05.12.14.
 */
public abstract class AbstractNeuralNet<T extends Instance> implements TrainingAlgorithm {

    protected final List<AbstractNeuralLayer> layers;
    protected final InputNeuralLayer inputLayer;
    protected final OutputNeuralLayer outputLayer;

    protected List<T> trainInstances;
    private double eps;

    private boolean useErrorModel;

    public AbstractNeuralNet(List<T> trainInstances, int inputs, int layerNumber, int[] layerSizes, ActivationFunction[] functions, double alpha, double eps) {
        this.trainInstances = Objects.requireNonNull(trainInstances);
        if (layerNumber < 1) {
            throw new IllegalArgumentException("layerNumber < 1");
        }
        if (layerSizes == null || layerSizes.length != layerNumber) {
            throw new IllegalArgumentException("layerSizes == null || layerSizes.length != layerNumber");
        }
        if (functions == null || functions.length != layerNumber) {
            throw new IllegalArgumentException("functions == null || functions.length != layerNumber");
        }
        inputLayer = new InputNeuralLayer(inputs);
        layers = new ArrayList<>(layerNumber + 1);
        layers.add(inputLayer);
        for (int i = 0; i < layerNumber - 1; i++) {
            InnerNeuralLayer innerLayer = new InnerNeuralLayer(layerSizes[i], functions[i]);
            layers.add(innerLayer);
        }
        outputLayer = new OutputNeuralLayer(layerSizes[layerNumber - 1], functions[layerNumber - 1]);
        layers.add(outputLayer);
        this.eps = eps;
        Random random = new Random();
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).nextLayer(layers.get(i + 1), alpha, random);
        }
    }

    public AbstractNeuralNet(int inputs, int layerNumber, int[] layerSizes, ActivationFunction[] functions, double[][][] weights) {
        if (layerNumber < 1) {
            throw new IllegalArgumentException("layerNumber < 1");
        }
        if (layerSizes == null || layerSizes.length != layerNumber) {
            throw new IllegalArgumentException("layerSizes == null || layerSizes.length != layerNumber");
        }
        if (functions == null || functions.length != layerNumber) {
            throw new IllegalArgumentException("functions == null || functions.length != layerNumber");
        }
        inputLayer = new InputNeuralLayer(inputs);
        layers = new ArrayList<>(layerNumber + 1);
        layers.add(inputLayer);
        for (int i = 0; i < layerNumber - 1; i++) {
            InnerNeuralLayer innerLayer = new InnerNeuralLayer(layerSizes[i], functions[i]);
            layers.add(innerLayer);
        }
        outputLayer = new OutputNeuralLayer(layerSizes[layerNumber - 1], functions[layerNumber - 1]);
        layers.add(outputLayer);
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).nextLayer(layers.get(i + 1), weights[i]);
        }
    }

    public void setParallel(boolean parallel) {
        layers.stream().forEach(l -> l.setParallel(parallel));
    }

    public AbstractNeuralNet<T> train() {
        if (trainInstances == null) {
            throw new UnsupportedOperationException();
        }
        List<T> errorInstances = new ArrayList<>();
        double prevMeasure = 0;
        double measure = 0;
        do {
            long start = System.currentTimeMillis();
            resetMeasure();
            prevMeasure = measure;
            for (T instance : errorInstances) {
                double[] result = calculateResult(instance);
                addResult(instance, result);
                updateWeights(instance);
            }
            errorInstances.clear();
            for (T instance : trainInstances) {
                double[] result = calculateResult(instance);
                if (addResult(instance, result) && useErrorModel) {
                    errorInstances.add(instance);
                }
                updateWeights(instance);
            }
            measure = getMeasure();
            long end = System.currentTimeMillis();
            System.out.println("measure: " + measure);
            System.out.println("time: " + (end - start));
        } while (Math.abs(measure - prevMeasure) > eps);
        return this;
    }

    public void useErrorModel(boolean useErrorModel) {
        this.useErrorModel = useErrorModel;
    }

    public double[] calculateResult(T instance) {
        inputLayer.setInputSignals(instance.getValues());
        layers.stream().forEach(AbstractNeuralLayer::propagateSignal);
        return outputLayer.getResult();
    }

    private void updateWeights(T instance) {
        outputLayer.setRealValues(getRealValues(instance));
        for (int i = layers.size() - 1; i > 0; i--) {
            layers.get(i).propagateError();
        }
    }

    public String dump() {
        StringBuilder builder = new StringBuilder();
        builder.append(layers.size() - 1).append('\n');
        builder.append(layers.get(0).getSize()).append('\n');
        List<String> functions = new ArrayList<>();
        for (int i = 1; i < layers.size(); i++) {
            functions.add(layers.get(i).getActivationFunction().getClass().getCanonicalName());
        }
        builder.append(String.join(" ", functions)).append('\n');
        List<String> layerSizes = new ArrayList<>();
        for (int i = 1; i < layers.size(); i++) {
            layerSizes.add(String.valueOf(layers.get(i).getSize()));
        }
        builder.append(String.join(" ", layerSizes)).append('\n');
        for (int i = 0; i < layers.size() - 1; i++) {
            builder.append(layers.get(i).dump()).append('\n');
        }
        return builder.toString();
    }

    protected abstract double getMeasure();
    protected abstract void resetMeasure();
    protected abstract boolean addResult(T instance, double[] res);
    protected abstract double[] getRealValues(T instance);

}

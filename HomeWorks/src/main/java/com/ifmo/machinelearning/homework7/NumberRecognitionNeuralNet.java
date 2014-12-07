package com.ifmo.machinelearning.homework7;

import com.ifmo.machinelearning.library.neural.AbstractNeuralNet;
import com.ifmo.machinelearning.library.neural.ActivationFunction;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by warrior on 06.12.14.
 */
public class NumberRecognitionNeuralNet extends AbstractNeuralNet<NumberImageInstance> {

    private int resultNumber;
    private int correctResult;

    public NumberRecognitionNeuralNet(List<NumberImageInstance> trainInstances, int inputs, int layerNumber, int[] layerSizes, ActivationFunction[] functions, double alpha, double eps) {
        super(trainInstances, inputs, layerNumber, layerSizes, functions, alpha, eps);
    }

    public NumberRecognitionNeuralNet(int inputs, int layerNumber, int[] layerSizes, ActivationFunction[] functions, double[][][] weights) {
        super(inputs, layerNumber, layerSizes, functions, weights);
    }

    @Override
    protected double getMeasure() {
        return ((double) correctResult) / resultNumber;
    }

    @Override
    protected void resetMeasure() {
        resultNumber = 0;
        correctResult = 0;
    }

    @Override
    protected boolean addResult(NumberImageInstance instance, double[] res) {
        resultNumber++;
        if (instance.getClassId() == getClassNumber(res)) {
            correctResult++;
            return false;
        }
        return true;
    }

    @Override
    protected double[] getRealValues(NumberImageInstance instance) {
        double[] realValues = new double[instance.getClassNumber()];
        for (int i = 0; i < instance.getClassNumber(); i++) {
            realValues[i] = i == instance.getClassId() ? 1 : -1;
        }
        return realValues;
    }

    public int getNumber(NumberImageInstance instance) {
        double[] res = calculateResult(instance);
        return getClassNumber(res);
    }

    private int getClassNumber(double[] res) {
        double max = -Double.MAX_VALUE;
        int classNumber = -1;
        for (int i = 0; i < 10; i++) {
            if (res[i] > max) {
                max = res[i];
                classNumber = i;
            }
        }
        return classNumber;
    }

    public static NumberRecognitionNeuralNet fromDump(String filename) {
        try (FastScanner scanner = new FastScanner(filename)){
            int layerNumber = scanner.nextInt();
            int inputs = scanner.nextInt();
            ActivationFunction[] functions = new ActivationFunction[layerNumber];
            for (int i = 0; i < layerNumber; i++) {
                String className = scanner.nextToken();
                functions[i] = (ActivationFunction) Class.forName(className).newInstance();
            }
            int[] layerSizes = new int[layerNumber];
            int[] sizes = new int[layerNumber + 1];
            sizes[0] = inputs;
            for (int i = 0; i < layerNumber; i++) {
                layerSizes[i] = scanner.nextInt();
                sizes[i + 1] = layerSizes[i];
            }
            double[][][] weights = new double[layerNumber][][];
            for (int i = 0; i < layerNumber; i++) {
                weights[i] = new double[sizes[i] + 1][sizes[i + 1]];
                for (int j = 0; j < sizes[i] + 1; j++) {
                    for (int k = 0; k < sizes[i + 1]; k++) {
                        weights[i][j][k] = scanner.nextDouble();
                    }
                }
            }
            return new NumberRecognitionNeuralNet(inputs, layerNumber, layerSizes, functions, weights);

        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class FastScanner implements Closeable {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner(String filename) throws FileNotFoundException {
            reader = new BufferedReader(new FileReader(filename));
        }

        public String nextToken() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        return null;
                    }
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(nextToken());
        }

        public double nextDouble() {
            return Double.parseDouble(nextToken());
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }
}

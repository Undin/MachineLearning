package com.ifmo.machinelearning.homework7;

import com.ifmo.machinelearning.library.neural.ActivationFunction;
import com.ifmo.machinelearning.library.neural.LogisticFunction;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ifmo.machinelearning.homework7.Main.*;

/**
 * Created by warrior on 06.12.14.
 */
public class ParameterSelection {

    private static final double EPS = 0.001;

    private static final int MIN_NEURONS = 1000;
    private static final int NEURONS_OFFSET = 100;
    private static final double MIN_ALPHA = 0.1;
    private static final double ALPHA_OFFSET = 0.1;

    private static final int NEURON_MAX_INDEX = 1;
    private static final int ALPHA_MAX_INDEX = 1;

    private static final int TRAIN_INSTANCE_NUMBER = 10000;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0000");

    public static void main(String[] args) {
        double[][] results = new double[NEURON_MAX_INDEX][ALPHA_MAX_INDEX]; // neurons x alpha
        List<NumberImageInstance> test = NumberImageInstance.createFromFiles(TEST_DATA, TEST_LABELS);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CountDownLatch latch = new CountDownLatch(NEURON_MAX_INDEX * ALPHA_MAX_INDEX);
        for (int i = 0; i < NEURON_MAX_INDEX; i++) {
            for (int j = 0; j < ALPHA_MAX_INDEX; j++) {
                executor.submit(new Task(latch, test, 0, 0, results));
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (PrintWriter writer = new PrintWriter("./HomeWorks/res/homework7/parameter_selection_result_1000.txt")) {
            writer.print(resultToString(results));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String resultToString(double[][] results) {
        StringBuilder builder = new StringBuilder();
        int rows = results.length;
        int columns = results[0].length;
        builder.append(emptyRow(columns, false));
        String[] alphas = new String[columns + 1];
        alphas[0] = "";
        for (int i = 0; i < columns; i++) {
            alphas[i + 1] = DECIMAL_FORMAT.format(MIN_ALPHA + i * ALPHA_OFFSET);
        }
        builder.append(nonemptyRow(alphas));
        builder.append(emptyRow(columns, true));
        for (int i = 0; i < rows; i++) {
            String[] values = new String[columns + 1];
            values[0] = String.valueOf(MIN_NEURONS + NEURONS_OFFSET * i);
            for (int j = 0; j < columns; j++) {
                values[j + 1] = DECIMAL_FORMAT.format(results[i][j]);
            }
            builder.append(emptyRow(columns, false));
            builder.append(nonemptyRow(values));
            builder.append(emptyRow(columns, true));
        }
        return builder.toString();
    }

    private static String nonemptyRow(String[] values) {
        StringBuilder builder = new StringBuilder();
        char[] spaces = new char[5 - values[0].length()];
        Arrays.fill(spaces, ' ');
        builder.append(spaces).append(values[0]).append(" |");
        for (int i = 1; i < values.length; i++) {
            builder.append(' ').append(values[i]).append(" |");
        }
        builder.append('\n');
        return builder.toString();
    }

    private static String emptyRow(int size, boolean isBottom) {
        StringBuilder builder = new StringBuilder(7 + 9 * size);
        char ch = isBottom ? '_' : ' ';
        builder.append(getSection(6, ch));
        for (int i = 0; i < size; i++) {
            builder.append(getSection(8, ch));
        }
        builder.append('\n');
        return builder.toString();
    }

    private static String getSection(int size, char ch) {
        StringBuilder builder = new StringBuilder();
        char[] section = new char[size];
        Arrays.fill(section, ch);
        builder.append(section).append('|');
        return builder.toString();
    }

    private static class Task implements Runnable {

        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");
        static {
            FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        }

        private final CountDownLatch latch;

        private final List<NumberImageInstance> test;
        private final int neuronIndex;
        private final int alphaIndex;
        private final double[][] results;

        public Task(CountDownLatch latch, List<NumberImageInstance> test, int neuronIndex, int alphaIndex, double[][] results) {
            this.latch = latch;
            this.test = test;
            this.neuronIndex = neuronIndex;
            this.alphaIndex = alphaIndex;
            this.results = results;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            Date date = new Date(start);
            System.out.println("date: " + FORMAT.format(date) + ", neurons: " + (MIN_NEURONS + NEURONS_OFFSET * neuronIndex) + ", alpha: " + (MIN_ALPHA + ALPHA_OFFSET * alphaIndex) + " start");

            List<NumberImageInstance> train = NumberImageInstance.createFromFiles(TRAIN_DATA, TRAIN_LABELS);
            Collections.shuffle(train);
            train = train.subList(0, TRAIN_INSTANCE_NUMBER);
            int[] layerSizes = {MIN_NEURONS + NEURONS_OFFSET * neuronIndex, 10};
            ActivationFunction[] functions = {new LogisticFunction(), new LogisticFunction()};
            double alpha = MIN_ALPHA + ALPHA_OFFSET * alphaIndex;
            NumberRecognitionNeuralNet net = new NumberRecognitionNeuralNet(train, 28 * 28, 2, layerSizes, functions, alpha, EPS);

            net.train();
            double accuracy = 0;
            for (NumberImageInstance image : test) {
                int res = net.getNumber(image);
                if (res == image.getClassId()) {
                    accuracy++;
                }
            }
            accuracy /= test.size();
            results[neuronIndex][alphaIndex] = accuracy;
            long end = System.currentTimeMillis();
            System.out.println("neurons: " + (MIN_NEURONS + NEURONS_OFFSET * neuronIndex) + ", alpha: " + (MIN_ALPHA + ALPHA_OFFSET * alphaIndex) + ", accuracy: " + accuracy + ", time: " + (end - start));
            latch.countDown();
        }
    }
}

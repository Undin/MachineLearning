package com.ifmo.machinelearning.homework7;

import com.ifmo.machinelearning.library.neural.ActivationFunction;
import com.ifmo.machinelearning.library.neural.BipolarSigmoid;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by warrior on 07.12.14.
 */
public class Test {

    public static final String TRAIN_DATA = "./HomeWorks/res/homework7/train-images-idx3-ubyte";
    public static final String TRAIN_LABELS = "./HomeWorks/res/homework7/train-labels-idx1-ubyte";
    public static final String TEST_DATA = "./HomeWorks/res/homework7/t10k-images-idx3-ubyte";
    public static final String TEST_LABELS = "./HomeWorks/res/homework7/t10k-labels-idx1-ubyte";

    private static final SimpleDateFormat DUMP_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss:SSS");

    private static final double ALPHA = 0.05;
    private static final double EPS = 0.001;
    private static final int INPUTS = 28 * 28;
    private static final int LAYERS = 2;
    private static final int HIDDEN_NEURONS = 700;

    private static final int PARTS = 5;

    public static void main(String[] args) {
        List<NumberImageInstance> train = NumberImageInstance.createFromFiles(TRAIN_DATA, TRAIN_LABELS);
        List<NumberImageInstance> test = NumberImageInstance.createFromFiles(TEST_DATA, TEST_LABELS);
        Collections.shuffle(train);
        int[] layerSizes = {HIDDEN_NEURONS, 10};
        ActivationFunction[] functions = {new BipolarSigmoid(), new BipolarSigmoid()};

        int partSize = train.size() / PARTS;
        NumberRecognitionNeuralNet[] nets = new NumberRecognitionNeuralNet[PARTS];
        for (int i = 0; i < PARTS; i++) {
            nets[i] = new NumberRecognitionNeuralNet(train.subList(partSize * i, partSize * (i + 1)), INPUTS, LAYERS, layerSizes, functions, ALPHA, EPS);
            nets[i].setParallel(true);
        }
        for (NumberRecognitionNeuralNet net : nets) {
            net.train();
        }
        double accuracy = 0;
        int[] vote = new int[10];
        for (NumberImageInstance instance : test) {
            Arrays.fill(vote, 0);
            for (NumberRecognitionNeuralNet net : nets) {
                vote[net.getNumber(instance)]++;
            }
            int max = -1;
            int number = -1;
            for (int i = 0; i < 10; i++) {
                if (vote[i] > max) {
                    max = vote[i];
                    number = i;
                }
            }
            if (number == instance.getClassId()) {
                accuracy++;
            }
        }
        accuracy /= test.size();
        System.out.println("test accuracy = " + accuracy);
        String date = DUMP_DATE_FORMAT.format(new Date());
        for (int i = 0; i < PARTS; i++) {
            String dumpFilename = "./HomeWorks/res/homework7/" + date + "_committee_" + i + '_' + HIDDEN_NEURONS + '_' + ALPHA + '_' + accuracy + ".txt";
            try (PrintWriter writer = new PrintWriter(dumpFilename)) {
                writer.print(nets[i].dump());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

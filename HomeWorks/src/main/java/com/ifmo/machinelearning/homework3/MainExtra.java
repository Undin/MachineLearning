package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.ClassifiedInstance;
import com.ifmo.machinelearning.library.InstanceCreator;
import com.ifmo.machinelearning.library.svm.GaussKernel;
import com.ifmo.machinelearning.library.test.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class MainExtra {

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 50;

    public static void main(String[] args) throws IOException {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");
        Collections.shuffle(sample);
        List<ClassifiedInstance> first = new ArrayList<>(sample.subList(0, sample.size() / 5));
        List<ClassifiedInstance> second = new ArrayList<>(sample.subList(sample.size() / 5, sample.size()));

        SVMTestMachine testMachine = new SVMTestMachine(second, true);
        double neededC = 0;
        double neededGamma = 0;
        double maxFMeasure = 0;
        for (int cPow = 1; cPow <= 7; cPow += 2) {
            for (int gammaPow = -1; gammaPow <= 3; gammaPow += 2) {
                double c = StrictMath.pow(2, cPow);
                double gamma = StrictMath.pow(2, gammaPow);
                testMachine.setKernel(new GaussKernel(gamma));
                testMachine.setC(c);
                Statistics statistics = testMachine.crossValidationTest(FOLD_NUMBER, ROUNDS);
                double fMeasure = statistics.getFMeasure();
                if (maxFMeasure < fMeasure) {
                    maxFMeasure = fMeasure;
                    neededGamma = gamma;
                    neededC = c;
                }
                System.out.println(String.format("F-Measure %f --- with C = %f and gamma = %f", fMeasure, c, gamma));
            }
        }
        testMachine.setC(neededC);
        testMachine.setKernel(new GaussKernel(neededGamma));
        Statistics statistics = testMachine.test(first);
        System.out.println(statistics.getFMeasure());
    }
}

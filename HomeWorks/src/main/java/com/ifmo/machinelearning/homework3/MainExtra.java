package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.test.Statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class MainExtra {

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 50;

    public static void main(String[] args) throws IOException {
        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("./HomeWorks/res/homework1/chips.txt"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ", ");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }

        Collections.shuffle(sample);
        List<Point> first = new ArrayList<>(sample.subList(0, sample.size() / 5));
        List<Point> second = new ArrayList<>(sample.subList(sample.size() / 5, sample.size()));

        SVMTestMachine testMachine = new SVMTestMachine(second, true);
        double neededC = 0;
        double neededGamma = 0;
        double maxFMeasure = 0;
        for (int cPow = 1; cPow <= 7; cPow += 2) {
            for (int gammaPow = -1; gammaPow <= 3; gammaPow += 2) {
                double c = StrictMath.pow(2, cPow);
                double gamma = StrictMath.pow(2, gammaPow);
                testMachine.setKernel(new RBFKernel(gamma));
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
        testMachine.setKernel(new RBFKernel(neededGamma));
        Statistics statistics = testMachine.test(first);
        System.out.println(statistics.getFMeasure());
    }

}

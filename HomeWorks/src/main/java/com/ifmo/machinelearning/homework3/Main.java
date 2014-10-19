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
public class Main {

    private static final int FOLD_NUMBER = 5;
    private static final int ROUNDS = 20;

    public static void main(String[] args) throws IOException {
        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("./HomeWorks/res/homework3/LinearDataset"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, " ");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }

        Collections.shuffle(sample);
        List<Point> first = new ArrayList<>(sample.subList(0, sample.size() / 5));
        List<Point> second = new ArrayList<>(sample.subList(sample.size() / 5, sample.size()));

        SVMTestMachine testMachine = new SVMTestMachine(second, true);
        testMachine.setKernel(FullPolyKernel.getInstance(3));
        double neededC = 0;
        double maxFMeasure = 0;
        for (int cPow = -5; cPow <= 15; cPow += 2) {
            double c = StrictMath.pow(2, cPow);
            testMachine.setC(c);
            Statistics statistics = testMachine.crossValidationTest(FOLD_NUMBER, ROUNDS);
            double fMeasure = statistics.getFMeasure();
            if (maxFMeasure < fMeasure) {
                maxFMeasure = fMeasure;
                neededC = c;
            }
            System.out.println("F-Measure " + fMeasure + " --- with C = " + c);
        }
        testMachine.setC(neededC);
        Statistics statistics = testMachine.test(first);
        System.out.println(statistics.getFMeasure());
    }

}

package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.test.Statistics;
import com.ifmo.machinelearning.test.TestMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<Point> sample = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("chips.txt"));
        String line;
        while ((line = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            int value = Integer.parseInt(st.nextToken());
            sample.add(new Point(x, y, value));
        }

        KNNTestMachine testMachine = new KNNTestMachine(TestMachine.TestType.CROSS_VALIDATION);
        int n = sample.size() / 5 * 4;
        double[] xAxis = new double[n - 1];
        double[] yAxis = new double[n - 1];
        for (int k = 1; k < n; k++) {
            testMachine.setK(k);
            Statistics statistics = testMachine.test(sample);
            xAxis[k - 1] = k;
            yAxis[k - 1] = statistics.getFDistance();
        }
        Plot2DBuilder builder = new Plot2DBuilder("k", "F-Measure");
        builder.addPlot("Test", xAxis, yAxis);
        builder.show();
    }

}

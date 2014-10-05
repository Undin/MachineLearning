package com.ifmo.machinelearning.homework3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class Main {

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
    }

}

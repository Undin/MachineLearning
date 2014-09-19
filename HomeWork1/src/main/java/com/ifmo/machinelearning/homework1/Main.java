package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.Distance;
import com.ifmo.machinelearning.library.KNN;
import com.ifmo.machinelearning.library.LinearWeight;

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

        Distance<Point, Double> distance = EuclideanDistance.getInstance();
        KNN<Point> knn = new KNN<>(sample, distance, new LinearWeight<>(distance), 10);
        System.out.println(knn.getSupposedClassId(new Point(10, 10)));
    }

}

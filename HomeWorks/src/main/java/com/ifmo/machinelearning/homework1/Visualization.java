package com.ifmo.machinelearning.homework1;

import com.ifmo.machinelearning.library.classifiers.knn.DistanceWeight;
import com.ifmo.machinelearning.library.classifiers.knn.KNN;
import com.ifmo.machinelearning.library.classifiers.knn.ManhattanDistance;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.visualization.Drawing;

import java.util.List;

/**
 * Created by Whiplash on 21.09.2014.
 */
public class Visualization extends Drawing {

    private static final int K = 6;

    @Override
    protected void initialize() {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");
        drawValues(new KNN(sample, new ManhattanDistance(), new DistanceWeight(), K), "x", "y");
    }

    @Override
    protected String getTitle() {
        return "KNN visualization";
    }

    @Override
    protected double getScale() {
        return 300;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
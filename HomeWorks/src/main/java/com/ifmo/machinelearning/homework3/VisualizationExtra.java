package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.classifiers.svm.GaussKernel;
import com.ifmo.machinelearning.library.classifiers.svm.SVMClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.visualization.Drawing;

import java.util.List;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class VisualizationExtra extends Drawing {

    private static final double C = 8;
    private static final double GAMMA = 0.5;

    @Override
    protected void initialize() {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework1/chips.txt");
        SVMClassifier classifier = new SVMClassifier(sample, new GaussKernel(GAMMA), C);
        classifier.train();
        drawValues(classifier, "x", "y");
        drawValues(sample);
    }

    @Override
    protected String getTitle() {
        return "SVM visualization";
    }

    @Override
    protected double getScale() {
        return 300;
    }

    @Override
    protected int getHeight() {
        return 800;
    }

    @Override
    protected int getWidth() {
        return 800;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.classifiers.svm.InnerProductKernel;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.test.Statistics;
import com.ifmo.machinelearning.visualization.Drawing;

import java.util.List;

/**
 * Created by Whiplash on 05.10.2014.
 */
public class Visualization extends Drawing {

    private static final double C = 2;

    @Override
    protected void initialize() {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework3/LinearDataset");
        SVMTestMachine testMachine = new SVMTestMachine(sample);
        testMachine.setC(C);
        Statistics statistics = testMachine.test(sample);
        System.out.println(statistics.getFMeasure());

        PointSVMClassifier classifier = new PointSVMClassifier(sample, new InnerProductKernel(), 2);
        classifier.train();
        System.out.println(String.format("y = %f * x + %f", classifier.getK(), classifier.getB()));

        drawAxis();
        drawLine(classifier.getK(), classifier.getB());
        drawValues(sample);
    }

    @Override
    protected String getTitle() {
        return "SVM visualization";
    }

    @Override
    protected double getScale() {
        return 30;
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

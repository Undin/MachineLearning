package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.Kernel2;
import com.ifmo.machinelearning.test.TestMachine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class SVMTestMachine extends TestMachine<Point> {

    private double c = 1;
    private Kernel2<Point> kernel;

    public SVMTestMachine(List<Point> dataSet) {
        super(dataSet);
    }

    public SVMTestMachine(List<Point> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setKernel(Kernel2<Point> kernel) {
        this.kernel = kernel;
    }

    @Override
    protected ExecutorService generateExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    @Override
    protected Classifier<Point> createClassifier(List<Point> dataSet) {
        return new PointSVMClassifier(dataSet, kernel, c);
    }

}

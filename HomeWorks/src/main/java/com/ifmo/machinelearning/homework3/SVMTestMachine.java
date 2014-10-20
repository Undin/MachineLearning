package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.library.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.ClassifiedInstance;
import com.ifmo.machinelearning.library.svm.InnerProductKernel;
import com.ifmo.machinelearning.library.svm.Kernel;
import com.ifmo.machinelearning.library.test.TestMachine;

import java.util.List;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class SVMTestMachine extends TestMachine {

    private double c = 1;
    private Kernel kernel;

    public SVMTestMachine(List<ClassifiedInstance> dataSet) {
        super(dataSet);
    }

    public SVMTestMachine(List<ClassifiedInstance> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
        kernel = new InnerProductKernel();
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    protected AbstractInstanceClassifier createClassifier(List<ClassifiedInstance> dataSet) {
        return new PointSVMClassifier(dataSet, kernel, c);
    }
}

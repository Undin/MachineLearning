package com.ifmo.machinelearning.library.test;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.List;

/**
 * Created by warrior on 20.10.14.
 */
public abstract class TestMachine extends AbstractTestMachine<ClassifiedInstance> {

    public TestMachine(List<ClassifiedInstance> dataSet) {
        super(dataSet);
    }

    public TestMachine(List<ClassifiedInstance> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    @Override
    protected abstract AbstractInstanceClassifier createClassifier(List<ClassifiedInstance> dataSet);
}

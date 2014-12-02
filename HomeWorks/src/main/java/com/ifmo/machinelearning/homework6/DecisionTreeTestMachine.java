package com.ifmo.machinelearning.homework6;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.classifiers.trees.DecisionTree;
import com.ifmo.machinelearning.library.classifiers.trees.IGain;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.test.TestMachine;

import java.util.List;

/**
 * Created by Whiplash on 02.12.2014.
 */
public class DecisionTreeTestMachine extends TestMachine {

    private int size = 1;

    public DecisionTreeTestMachine(List<ClassifiedInstance> dataSet) {
        super(dataSet);
    }

    public DecisionTreeTestMachine(List<ClassifiedInstance> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    protected AbstractInstanceClassifier createClassifier(List<ClassifiedInstance> dataSet) {
        return new DecisionTree(dataSet, IGain.getInstance(), size);
    }
}

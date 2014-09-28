package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.bayes.Bayes;
import com.ifmo.machinelearning.test.TestMachine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Whiplash on 19.09.2014.
 */
public class BayesTestMachine extends TestMachine<Message> {

    public BayesTestMachine(List<Message> dataSet) {
        super(dataSet);
    }

    public BayesTestMachine(List<Message> dataSet, boolean parallelTest) {
        super(dataSet, parallelTest);
    }

    @Override
    protected ExecutorService generateExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    @Override
    protected Classifier<Message> createClassifier(List<Message> dataSet) {
        return new Bayes<>(dataSet);
    }

}

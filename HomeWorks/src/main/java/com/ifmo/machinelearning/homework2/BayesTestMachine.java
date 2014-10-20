//package com.ifmo.machinelearning.homework2;
//
//import com.ifmo.machinelearning.library.classifiers.AbstractClassifier;
//import com.ifmo.machinelearning.library.test.AbstractTestMachine;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by Whiplash on 19.09.2014.
// */
//public class BayesTestMachine extends AbstractTestMachine<Message> {
//
//    public BayesTestMachine(List<Message> dataSet) {
//        super(dataSet);
//    }
//
//    public BayesTestMachine(List<Message> dataSet, boolean parallelTest) {
//        super(dataSet, parallelTest);
//    }
//
//    @Override
//    protected ExecutorService generateExecutorService() {
//        return Executors.newFixedThreadPool(3);
//    }
//
//    @Override
//    protected AbstractClassifier<Message> createClassifier(List<Message> dataSet) {
//        return new NaiveBayesClassifier(dataSet);
//    }
//}

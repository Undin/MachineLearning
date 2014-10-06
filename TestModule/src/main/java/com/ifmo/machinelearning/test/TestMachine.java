package com.ifmo.machinelearning.test;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by warrior on 19.09.14.
 */
public abstract class TestMachine<T extends ClassifiedData> {

    protected List<T> dataSet;

    private boolean parallelTest;

    private int size;
    private int classNumber;
    private int[][] testConfusionMatrix;

    public TestMachine(List<T> dataSet) {
        this(dataSet, false);
    }

    public TestMachine(List<T> dataSet, boolean parallelTest) {
        checkNotEmptyData(dataSet);
        this.dataSet = new ArrayList<>(dataSet);
        this.parallelTest = parallelTest;
        size = dataSet.size();
        classNumber = dataSet.get(0).getClassNumber();
        testConfusionMatrix = new int[classNumber][classNumber];
    }

    public Statistics crossValidationTest(int foldCount, int rounds) {
        clearConfusionMatrix();
        final int foldSize = size / foldCount;
        for (int k = 0; k < rounds; k++) {
            Collections.shuffle(dataSet);
            List<T> trainingData = new ArrayList<>(size - foldSize);
            List<T> testData;
            for (int i = 0; i < foldCount; i++) {
                trainingData.clear();
                int l = i * foldSize;
                int r = l + foldSize;
                testData = dataSet.subList(l, r);
                trainingData.addAll(dataSet.subList(0, l));
                trainingData.addAll(dataSet.subList(r, size));
                testInternal(trainingData, testData);
            }
        }
        return getCurrentStatistic();
    }

    public Statistics randomSubSamplingTest(int foldSize, int rounds) {
        clearConfusionMatrix();
        for (int i = 0; i < rounds; i++) {
            Collections.shuffle(dataSet);
            List<T> testData = dataSet.subList(0, foldSize);
            List<T> trainingData = dataSet.subList(foldSize, size);
            testInternal(trainingData, testData);
        }

        return getCurrentStatistic();
    }

    public Statistics leaveOneOut() {
        clearConfusionMatrix();
        List<T> trainingData = new ArrayList<>(size - 1);
        for (int i = 0; i < size; i++) {
            trainingData.clear();
            List<T> testData = Arrays.asList(dataSet.get(i));
            trainingData.addAll(dataSet.subList(0, i));
            trainingData.addAll(dataSet.subList(i + 1, size));
            testInternal(trainingData, testData);
        }
        return getCurrentStatistic();
    }

    public Statistics test(List<T> testData) {
        return testInternal(dataSet, testData);
    }

    protected Statistics testInternal(List<T> trainingData, List<T> testData) {
        clearConfusionMatrix();
        Classifier<T> classifier = createClassifier(trainingData).training();
        if (parallelTest) {
            testInternal(classifier, testData, testConfusionMatrix);
        } else {
            parallelTestInternal(classifier, testData, testConfusionMatrix);
        }
        return getCurrentStatistic();
    }

    protected void testInternal(Classifier<T> classifier, List<T> dataSet, int[][] confusionMatrix) {
        for (T t : dataSet) {
            confusionMatrix[classifier.getSupposedClassId(t)][t.getClassId()]++;
        }
    }

    protected void parallelTestInternal(Classifier<T> classifier, List<T> dataSet, int[][] confusionMatrix) {
        ExecutorService executor = generateExecutorService();
        List<Future<Integer>> futureList = new ArrayList<>(dataSet.size());
        for (T t : dataSet) {
            futureList.add(executor.submit(() -> classifier.getSupposedClassId(t)));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            for (int i = 0; i < dataSet.size(); i++) {
                confusionMatrix[futureList.get(i).get()][dataSet.get(i).getClassId()]++;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected ExecutorService generateExecutorService() {
        return Executors.newCachedThreadPool();
    }

    public void clearConfusionMatrix() {
        for (int[] arr : testConfusionMatrix) {
            Arrays.fill(arr, 0);
        }
    }

    public Statistics getCurrentStatistic() {
        return Statistics.createStatistics(testConfusionMatrix);
    }

    private static void checkNotEmptyData(List<?> dataSet) {
        if (dataSet == null || dataSet.isEmpty()) {
            throw new IllegalArgumentException("DataSet must be not empty");
        }
    }

    public boolean isParallelTest() {
        return parallelTest;
    }

    public void setParallelTest(boolean parallelTest) {
        this.parallelTest = parallelTest;
    }

    protected abstract Classifier<T> createClassifier(List<T> dataSet);

}

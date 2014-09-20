package com.ifmo.machinelearning.test;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;

import java.util.*;

/**
 * Created by warrior on 19.09.14.
 */
public abstract class TestMachine<T extends ClassifiedData> {

    protected List<T> dataSet;

    private int size;
    private int classNumber;
    private int[][] testConfusionMatrix;
    private int[][] trainingConfusionMatrix;

    public TestMachine(List<T> dataSet) {
        checkNotEmptyData(dataSet);
        this.dataSet = dataSet;
        size = dataSet.size();
        classNumber = dataSet.get(0).getClassNumber();
        testConfusionMatrix = new int[classNumber][classNumber];
        trainingConfusionMatrix = new int[classNumber][classNumber];
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
                test(trainingData, testData);
            }
        }
        return getCurrentStatistic();
    }

    public Statistics randomSubSamplingTest(int foldSize, int round) {
        clearConfusionMatrix();
        for (int i = 0; i < round; i++) {
            Collections.shuffle(dataSet);
            List<T> testData = dataSet.subList(0, foldSize);
            List<T> trainingData = dataSet.subList(foldSize, size);
            test(trainingData, testData);
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
            test(trainingData, testData);
        }
        return getCurrentStatistic();
    }

    protected void test(List<T> trainingData, List<T> testData) {
        Classifier<T> classifier = createClassifier(trainingData).training();
        testInternal(classifier, trainingData, trainingConfusionMatrix);
        testInternal(classifier, testData, testConfusionMatrix);
    }

    protected void testInternal(Classifier<T> classifier, List<T> dataSet, int[][] confusionMatrix) {
        for (T t : dataSet) {
            int supposedClass = classifier.getSupposedClassId(t);
            confusionMatrix[supposedClass][t.getClassId()]++;
        }
    }

    protected void clearConfusionMatrix() {
        for (int[] arr : testConfusionMatrix) {
            Arrays.fill(arr, 0);
        }
        for (int[] arr : trainingConfusionMatrix) {
            Arrays.fill(arr, 0);
        }
    }

    private Statistics getCurrentStatistic() {
        return Statistics.createStatistics(testConfusionMatrix, trainingConfusionMatrix);
    }

    private static void checkNotEmptyData(List<?> dataSet) {
        if (dataSet == null || dataSet.isEmpty()) {
            throw new IllegalArgumentException("DataSet must be not empty");
        }
    }

    protected abstract Classifier<T> createClassifier(List<T> dataSet);
}

package com.ifmo.machinelearning.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by warrior on 19.09.14.
 */
public abstract class TestMachine<T extends ClassifiedData> {

    public enum TestType {
        CROSS_VALIDATION,
        RANDOM_SUBSAMPLING,
        LEAVE_ONE_OUT
    }

    private static final int FOLD_COUNT = 5;

    private final TestType testType;

    public TestMachine(TestType testType) {
        this.testType = testType;
    }

    abstract protected Classifier<T> createClassifier(List<T> dataSet);

    public Statistics test(List<T> dataSet) {
        if (dataSet == null || dataSet.isEmpty()) {
            throw new IllegalArgumentException("dataSet must be not empty");
        }
        switch (testType) {
            case CROSS_VALIDATION:
                return crossValidationTest(dataSet);
            case RANDOM_SUBSAMPLING:
                return randomSubsamplingTest(dataSet);
            case LEAVE_ONE_OUT:
                return leaveOneOut(dataSet);
            default:
                return null;
        }
    }

    private Statistics crossValidationTest(List<T> dataSet) {
        final int foldSize = dataSet.size() / FOLD_COUNT;
        List<T> trainingData = new ArrayList<>(dataSet.size() - foldSize);
        List<T> testData = new ArrayList<>(foldSize);
        int classNumber = dataSet.get(0).getClassNumber();
        int[][] confusionMatrix = new int[classNumber][classNumber];
        for (int i = 0; i < FOLD_COUNT; i++) {
            trainingData.clear();
            testData.clear();
            int l = i * foldSize;
            int r = l + foldSize;
            testData.addAll(dataSet.subList(l, r));
            trainingData.addAll(dataSet.subList(0, l));
            trainingData.addAll(dataSet.subList(r, dataSet.size()));
            Classifier<T> classifier = createClassifier(trainingData).training();
            for (T t : testData) {
                int supposedClass = classifier.getSupposedClassId(t);
                confusionMatrix[supposedClass][t.getClassId()]++;
            }
        }
        return new Statistics(confusionMatrix);
    }

    private Statistics randomSubsamplingTest(List<T> dataSet) {
        return null;
    }

    private Statistics leaveOneOut(List<T> dataSet) {
        return null;
    }
}

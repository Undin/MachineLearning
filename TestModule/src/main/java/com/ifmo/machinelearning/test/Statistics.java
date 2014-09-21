package com.ifmo.machinelearning.test;

/**
 * Created by warrior on 19.09.14.
 */
public class Statistics {

    private double testFDistance;
    private double testPrecision;
    private double testRecall;
    private double trainingFDistance;
    private double trainingPrecision;
    private double trainingRecall;
    private boolean hasTrainingStatistic;

    public Statistics(double testPrecision, double testRecall) {
        this.testPrecision = testPrecision;
        this.testRecall = testRecall;
        testFDistance = computeFDistance(testPrecision, testRecall);
    }

    public Statistics(double testPrecision, double testRecall, double trainingPrecision, double trainingRecall) {
        this.testPrecision = testPrecision;
        this.testRecall = testRecall;
        testFDistance = computeFDistance(testPrecision, testRecall);
        this.trainingPrecision = trainingPrecision;
        this.trainingRecall = trainingRecall;
        trainingFDistance = computeFDistance(trainingPrecision, trainingRecall);
        hasTrainingStatistic = true;
    }

    private static double computeFDistance(double precision, double recall) {
        return 2 * precision * recall / (precision + recall);
    }

    public double getTestFDistance() {
        return testFDistance;
    }

    public double getTestPrecision() {
        return testPrecision;
    }

    public double getTestRecall() {
        return testRecall;
    }

    public double getTrainingFDistance() {
        checkTrainingStatistics();
        return trainingFDistance;
    }

    public double getTrainingPrecision() {
        checkTrainingStatistics();
        return trainingPrecision;
    }

    public double getTrainingRecall() {
        checkTrainingStatistics();
        return trainingRecall;
    }

    private void checkTrainingStatistics() {
        if (!hasTrainingStatistic) {
            throw new IllegalStateException("Statistics hasn't training statistics");
        }
    }

    public static Statistics createStatistics(int[][] testConfusionMatrix) {
        checkConfusionMatrix(testConfusionMatrix);
        double[] answer = new double[2];
        computePrecisionAndRecall(testConfusionMatrix, answer);
        return new Statistics(answer[0], answer[1]);
    }

    public static Statistics createStatistics(int[][] testConfusionMatrix, int[][] trainingConfusionMatrix) {
        checkConfusionMatrix(testConfusionMatrix);
        checkConfusionMatrix(trainingConfusionMatrix);
        double[] testAnswer = new double[2];
        double[] trainingAnswer = new double[2];
        computePrecisionAndRecall(testConfusionMatrix, testAnswer);
        computePrecisionAndRecall(trainingConfusionMatrix, trainingAnswer);
        return new Statistics(testAnswer[0], testAnswer[1], trainingAnswer[0], trainingAnswer[1]);
    }

    private static void checkConfusionMatrix(int[][] confusionMatrix) {
        if (confusionMatrix == null) {
            throw new IllegalArgumentException("confusionMatrix must be not null");
        }
        if (confusionMatrix.length != confusionMatrix[0].length) {
            throw new IllegalArgumentException("confusionMatrix must be square");
        }
    }

    /**
     *
     * @param confusionMatrix
     * @param ans answer array. After method's execution {@code ans[0]} contains precision,
     *        {@code ans[1]} contains recall.
     */
    private static void computePrecisionAndRecall(int[][] confusionMatrix, double[] ans) {
        if (confusionMatrix == null) {
            throw new IllegalArgumentException("confusionMatrix must be not null");
        }
        if (confusionMatrix.length != confusionMatrix[0].length) {
            throw new IllegalArgumentException("confusionMatrix must be square");
        }
        if (ans == null || ans.length < 2) {
            throw new IllegalArgumentException("answer array length must be >= 2");
        }
        int size = confusionMatrix.length;
        int precisionCount = 0;
        int recallCount = 0;
        ans[0] = 0;
        ans[1] = 0;
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += confusionMatrix[i][j];
            }
            if (sum != 0) {
                ans[0] += confusionMatrix[i][i] / sum;
                precisionCount++;
            }
            sum = 0;
            for (int j = 0; j < size; j++) {
                sum += confusionMatrix[j][i];
            }
            if (sum != 0) {
                ans[1] += confusionMatrix[i][i] / sum;
                recallCount++;
            }

        }
        ans[0] /= precisionCount;
        ans[1] /= recallCount;
    }
}

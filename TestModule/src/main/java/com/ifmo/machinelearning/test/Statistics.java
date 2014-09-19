package com.ifmo.machinelearning.test;

/**
 * Created by warrior on 19.09.14.
 */
public class Statistics {

    private double fDistance;
    private double precision;
    private double recall;

    public Statistics(double precision, double recall) {
        this.precision = precision;
        this.recall = recall;
        fDistance = computeFDistance(precision, recall);
    }

    public Statistics(int[][] confusionMatrix) {
        if (confusionMatrix == null) {
            throw new IllegalArgumentException("confusionMatrix must be not null");
        }
        if (confusionMatrix.length != confusionMatrix[0].length) {
            throw new IllegalArgumentException("confusionMatrix must be square");
        }

        int size = confusionMatrix.length;
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += confusionMatrix[i][j];
            }
            precision += confusionMatrix[i][i] / sum;
            for (int j = 0; j < size; j++) {
                sum = confusionMatrix[j][i];
            }
            recall += confusionMatrix[i][i] / sum;
        }
        precision /= size;
        recall /= size;
        fDistance = computeFDistance(precision, recall);
    }

    private static double computeFDistance(double precision, double recall) {
        return 2 * precision * recall / (precision + recall);
    }

    public double getfDistance() {
        return fDistance;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }
}

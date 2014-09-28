package com.ifmo.machinelearning.library.bayes;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;

import java.util.List;

/**
 * Created by warrior on 29.09.14.
 */
public abstract class BayesClassifier<T extends ClassifiedData> extends Classifier<T> {

    private double prioriProbability[];

    public BayesClassifier(List<T> data) {
        super(data);
    }

    @Override
    public Classifier<T> training() {
        calculatePrioriProbability();
        calculateCredibilityFunction();
        return this;
    }

    protected void calculatePrioriProbability() {
        prioriProbability = new double[getClassNumber()];
        getData().forEach(t -> prioriProbability[t.getClassId()] += 1);
        for (int i = 0; i < getClassNumber(); i++) {
            prioriProbability[i] /= getClassNumber();
        }
    }

    @Override
    public int getSupposedClassId(T t) {
        int supposedClassId = 0;
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < getClassNumber(); i++) {
            double value = prioriProbability[i] * credibilityFunction(t, i);
            if (value > maxValue) {
                supposedClassId = i;
                maxValue = value;
            }
        }
        return supposedClassId;
    }

    protected abstract double calculateCredibilityFunction();

    protected abstract double credibilityFunction(T t, int classId);
}

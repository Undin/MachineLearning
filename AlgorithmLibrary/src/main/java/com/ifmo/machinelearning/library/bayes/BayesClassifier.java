package com.ifmo.machinelearning.library.bayes;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;

import java.util.List;

/**
 * Created by warrior on 29.09.14.
 */
public abstract class BayesClassifier<T extends ClassifiedData> extends Classifier<T> {

    private double lnPrioriProbability[];

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
        lnPrioriProbability = new double[getClassNumber()];
        getData().forEach(t -> lnPrioriProbability[t.getClassId()] += 1);
        for (int i = 0; i < getClassNumber(); i++) {
            lnPrioriProbability[i] = StrictMath.log(lnPrioriProbability[i]) - StrictMath.log(getData().size());
        }
    }

    @Override
    public int getSupposedClassId(T t) {
        int supposedClassId = 0;
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < getClassNumber(); i++) {
            double value = lnPrioriProbability[i] + credibilityFunctionLn(t, i);
            if (value > maxValue) {
                supposedClassId = i;
                maxValue = value;
            }
        }
        return supposedClassId;
    }

    protected abstract void calculateCredibilityFunction();

    protected abstract double credibilityFunctionLn(T t, int classId);
}

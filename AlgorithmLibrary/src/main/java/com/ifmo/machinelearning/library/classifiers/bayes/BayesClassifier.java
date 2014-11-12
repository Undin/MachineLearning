package com.ifmo.machinelearning.library.classifiers.bayes;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.List;

import static java.lang.StrictMath.log;

/**
 * Created by warrior on 29.09.14.
 */
public abstract class BayesClassifier extends AbstractInstanceClassifier {

    private double lnPrioriProbability[];

    public BayesClassifier(List<ClassifiedInstance> data) {
        super(data);
    }

    @Override
    protected void trainInternal() {
        calculatePrioriProbability();
        calculateCredibilityFunction();
    }

    protected void calculatePrioriProbability() {
        lnPrioriProbability = new double[getClassNumber()];
        getData().forEach(t -> lnPrioriProbability[t.getClassId()] += 1);
        for (int i = 0; i < getClassNumber(); i++) {
            lnPrioriProbability[i] = log(lnPrioriProbability[i]) - log(size);
        }
    }

    @Override
    public int getSupposedClassId(ClassifiedInstance instance) {
        int supposedClassId = 0;
        double maxValue = -Double.MAX_VALUE;
        for (int i = 0; i < getClassNumber(); i++) {
            double value = lnPrioriProbability[i] + credibilityFunctionLn(instance, i);
            if (value > maxValue) {
                supposedClassId = i;
                maxValue = value;
            }
        }
        return supposedClassId;
    }

    protected abstract void calculateCredibilityFunction();

    protected abstract double credibilityFunctionLn(ClassifiedInstance instance, int classId);
}

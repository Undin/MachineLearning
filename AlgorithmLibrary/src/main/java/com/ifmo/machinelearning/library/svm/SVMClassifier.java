package com.ifmo.machinelearning.library.svm;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.Kernel2;

import java.util.List;
import java.util.Random;

/**
 * Created by warrior on 19.10.14.
 */
public class SVMClassifier<T extends ClassifiedData> extends Classifier<T> {

    private static final int MAX_PASSES = 1;
    private static final double EPS = 10e-5;

    private final Random random = new Random();
    private int[] modifiedClassIds;

    protected final Kernel2<T> kernelFunction;
    protected final double c;
    protected double[] alphas;
    protected double b;

    public SVMClassifier(List<T> data, Kernel2<T> kernelFunction, double c) {
        super(data);
        this.kernelFunction = kernelFunction;
        this.c = c;
    }

    @Override
    public Classifier<T> training() {
        modifiedClassIds = new int[getData().size()];
        for (int i = 0; i < getData().size(); i++) {
            int classId = getData().get(i).getClassId();
            modifiedClassIds[i] = classId == 0 ? -1 : classId;
        }
        alphas = smo();
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        double result = function(t);
        return result < 0 ? 0 : 1;
    }

    private double[] smo() {
        int size = getData().size();
        alphas = new double[size];
        b = 0;
        int passes = 0;
        while (passes < MAX_PASSES) {
            boolean hasChanged = false;
            for (int i = 0; i < size; i++) {
                double ei = calculateE(i);
                if (modifiedClassIds[i] * ei < -EPS && alphas[i] < c ||
                    modifiedClassIds[i] * ei >  EPS && alphas[i] > 0) {
                    int j;
                    do {
                        j = random.nextInt(size);
                    } while (j == i);
                    double ej = calculateE(j);
                    double oldAlphaI = alphas[i];
                    double oldAlphaJ = alphas[j];
                    double l, h;
                    if (modifiedClassIds[i] != modifiedClassIds[j]) {
                        l = Math.max(0, oldAlphaJ - oldAlphaI);
                        h = Math.min(c, c + oldAlphaJ - oldAlphaI);
                    } else {
                        l = Math.max(0, oldAlphaI + oldAlphaJ - c);
                        h = Math.min(c, oldAlphaI + oldAlphaJ);
                    }
                    if (l == h) {
                        continue;
                    }
                    T xi = getData().get(i);
                    T xj = getData().get(j);
                    double n = 2 * kernelFunction.eval(xi, xj) - kernelFunction.eval(xi, xi) - kernelFunction.eval(xj, xj);
                    if (n >= 0) {
                        continue;
                    }
                    alphas[j] -= modifiedClassIds[j] * (ei - ej) / n;
                    if (alphas[j] > h) {
                        alphas[j] = h;
                    } else if (alphas[j] < l) {
                        alphas[j] = l;
                    }
                    if (Math.abs(alphas[j] - oldAlphaJ) < EPS) {
                        continue;
                    }
                    alphas[i] += modifiedClassIds[i] * modifiedClassIds[j] * (oldAlphaJ - alphas[j]);
                    double b1 = b - ei - modifiedClassIds[i] * (alphas[i] - oldAlphaI) * kernelFunction.eval(xi, xi) -
                                         modifiedClassIds[j] * (alphas[j] - oldAlphaJ) * kernelFunction.eval(xi, xj);
                    double b2 = b - ej - modifiedClassIds[i] * (alphas[i] - oldAlphaI) * kernelFunction.eval(xi, xj) -
                            modifiedClassIds[j] * (alphas[j] - oldAlphaJ) * kernelFunction.eval(xj, xj);
                    if (alphas[i] > 0 && alphas[i] < c) {
                        b = b1;
                    } else if (alphas[j] > 0 && alphas[j] < c) {
                        b = b2;
                    } else {
                        b = (b1 + b2) / 2;
                    }
                    hasChanged = true;
                }
            }
            if (!hasChanged) {
                passes++;
            } else {
                passes = 0;
            }
        }
        return alphas;
    }

    private double calculateE(int i) {
        return function(getData().get(i)) - modifiedClassIds[i];
    }

    protected double function(T t) {
        double result = 0;
        for (int i = 0; i < getData().size(); i++) {
            T point = getData().get(i);
            result += alphas[i] * modifiedClassIds[i] * kernelFunction.eval(point, t);
        }
        result += b;
        return result;
    }
}

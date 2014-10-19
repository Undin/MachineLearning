package com.ifmo.machinelearning.library.svm;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;
import com.ifmo.machinelearning.library.Kernel2;

import java.util.List;

/**
 * Created by warrior on 19.10.14.
 */
public class SVMClassifier<T extends ClassifiedData> extends Classifier<T> {

    private static final int MAX_PASSES = 5;

    private int[] modifiedClassIds;

    protected final Kernel2<T> kernelFunction;
    protected double[] alphas;
    protected double b;
    protected double c;

    public SVMClassifier(List<T> data, Kernel2<T> kernelFunction) {
        super(data);
        this.kernelFunction = kernelFunction;
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

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public int getSupposedClassId(T t) {
        double result = function(t);
        return result < 0 ? 0 : 1;
    }

    private double[] smo() {
//        int size = getData().size();
//        alphas = new double[size];
//        b = 0;
//        int passes = 0;
//        while (passes < MAX_PASSES) {
//            boolean hasChanged = false;
//            for (int i = 0; i < size; i++) {
//                double ei = function(getData().get(i)) - modifiedClassIds[i];
//            }
//        }
//        return alphas;
        return null;
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

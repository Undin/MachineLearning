package com.ifmo.machinelearning.library.classifiers;

import com.ifmo.machinelearning.library.core.ClassifiedData;
import com.ifmo.machinelearning.library.core.TrainingAlgorithm;

import java.util.List;

/**
 * Interface class that has the following methods:
 * {@link #train()} and {@link #getSupposedClassId(com.ifmo.machinelearning.library.core.ClassifiedData)}
 * <p>
 * Created by warrior on 19.09.14.
 */
public abstract class AbstractClassifier<T extends ClassifiedData> implements TrainingAlgorithm {

    protected final List<T> data;
    protected final int size;
    protected final int classNumber;

    public AbstractClassifier(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data must be not empty");
        }
        this.data = data;
        this.size = data.size();
        this.classNumber = data.get(0).getClassNumber();
    }

    protected List<T> getData() {
        return data;
    }

    protected T get(int i) {
        return data.get(i);
    }

    protected int getClassNumber() {
        return classNumber;
    }

    /**
     * Trains algorithm (if it need)
     *
     * @return {@link AbstractClassifier} trained algorithm
     */
    public AbstractClassifier<T> train() {
        trainInternal();
        return this;
    }

    protected abstract void trainInternal();

    /**
     * Returns identifier of class of {@code t}
     * <p>
     * (ATTENTION call {@link #train()} before if it need)
     *
     * @param t required element
     * @return class id of {@code t}
     */
    public abstract int getSupposedClassId(T t);
}

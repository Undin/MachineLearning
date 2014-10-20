package com.ifmo.machinelearning.library;

import java.util.List;

/**
 * Interface class that has the following methods:
 * {@link #training()} and {@link #getSupposedClassId(ClassifiedData)}
 * <p>
 * Created by warrior on 19.09.14.
 */
public abstract class AbstractClassifier<T extends ClassifiedData> {

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

    protected int getClassNumber() {
        return classNumber;
    }

    /**
     * Trains algorithm (if it need)
     *
     * @return {@link AbstractClassifier} trained algorithm
     */
    public AbstractClassifier<T> training() {
        trainingInternal();
        return this;
    }

    protected abstract void trainingInternal();

    /**
     * Returns identifier of class of {@code t}
     * <p>
     * (ATTENTION call {@link #training()} before if it need)
     *
     * @param t required element
     * @return class id of {@code t}
     */
    public abstract int getSupposedClassId(T t);
}

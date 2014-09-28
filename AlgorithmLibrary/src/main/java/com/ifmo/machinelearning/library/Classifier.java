package com.ifmo.machinelearning.library;

import java.util.List;

/**
 * Interface class that has the following methods:
 * {@link #training()} and {@link #getSupposedClassId(ClassifiedData)}
 * <p>
 * Created by warrior on 19.09.14.
 */
public abstract class Classifier<T extends ClassifiedData> {

    private final List<T> data;
    private final int classNumber;

    public Classifier(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data must be not empty");
        }
        this.data = data;
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
     * @return {@link com.ifmo.machinelearning.library.Classifier} trained algorithm
     */
    public abstract Classifier<T> training();

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

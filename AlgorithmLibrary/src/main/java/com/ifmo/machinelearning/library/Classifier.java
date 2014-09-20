package com.ifmo.machinelearning.library;

/**
 * Interface class that has the following methods:
 * {@link #training()} and {@link #getSupposedClassId(ClassifiedData)}
 * <p>
 * Created by warrior on 19.09.14.
 */
public interface Classifier<T extends ClassifiedData> {

    /**
     * Trains algorithm (if it need)
     *
     * @return {@link com.ifmo.machinelearning.library.Classifier} trained algorithm
     */
    public Classifier<T> training();

    /**
     * Returns identifier of class of {@code t}
     * <p>
     * (ATTENTION call {@link #training()} before if it need)
     *
     * @param t required element
     * @return class id of {@code t}
     */
    public int getSupposedClassId(T t);

}

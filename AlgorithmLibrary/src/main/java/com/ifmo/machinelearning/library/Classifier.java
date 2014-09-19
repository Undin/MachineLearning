package com.ifmo.machinelearning.library;

/**
 * Created by warrior on 19.09.14.
 */
public interface Classifier<T extends ClassifiedData> {

    public Classifier<T> training();

    public int getSupposedClassId(T t);

}

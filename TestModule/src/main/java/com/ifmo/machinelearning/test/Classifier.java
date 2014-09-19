package com.ifmo.machinelearning.test;

/**
 * Created by warrior on 19.09.14.
 */
public interface Classifier<T> {

    public void training();

    public int getSupposedClassId(T t);

}

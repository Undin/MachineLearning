package com.ifmo.machinelearning.library;

/**
 * Created by Whiplash on 19.10.2014.
 */
public interface Kernel2<T> {

    public double eval(T first, T second);

}

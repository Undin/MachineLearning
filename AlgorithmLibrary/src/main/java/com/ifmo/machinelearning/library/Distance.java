package com.ifmo.machinelearning.library;

/**
 * Interface class that has the following methods:
 * {@link Distance#distance(Object, Object)}
 * <p/>
 * Created by Whiplash on 15.09.2014.
 */
public interface Distance<T, E> {

    /**
     * Measures distance between {@code first} and {@code second}
     *
     * @param objects distance between which measured
     * @return calculated distance
     */
    public E distance(T first, T second);

}

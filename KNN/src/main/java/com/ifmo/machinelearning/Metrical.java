package com.ifmo.machinelearning;

/**
 * Interface class that has the following methods:
 * {@link com.ifmo.machinelearning.Metrical#distanceTo(Object)}
 * <p/>
 * Created by Whiplash on 15.09.2014.
 */
public interface Metrical<T, E> {

    /**
     * Measures distance to {@code other}
     *
     * @param other object to which distance is measured
     * @return calculated distance
     */
    public E distanceTo(T other);

}

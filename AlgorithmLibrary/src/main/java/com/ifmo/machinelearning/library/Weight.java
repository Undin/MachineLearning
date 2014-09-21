package com.ifmo.machinelearning.library;

/**
 * Interface class that has the following methods:
 * {@link #weight(Object)}
 * <p>
 * Created by Whiplash on 19.09.2014.
 */
public interface Weight {

    /**
     * Measures weight between {@code first} and {@code second}
     *
     * @param distance distance between objects
     * @return calculated weight
     */
    public double weight(double distance);

}

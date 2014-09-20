package com.ifmo.machinelearning.library;

/**
 * Interface class that has the following methods:
 * {@link #weight(Object, Object)}
 * <p>
 * Created by Whiplash on 19.09.2014.
 */
public interface Weight<T, E> {

    /**
     * Measures weight between {@code first} and {@code second}
     *
     * @param first  one of required elements
     * @param second another element
     * @return calculated weight
     */
    public E weight(T first, T second);

}

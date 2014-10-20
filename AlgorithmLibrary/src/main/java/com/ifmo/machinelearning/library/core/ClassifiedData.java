package com.ifmo.machinelearning.library.core;

/**
 * Interface that has the following methods:
 * {@link #getClassId()} and {@link #getClassId()}
 * <p>
 * Created by warrior on 19.09.14.
 */
public interface ClassifiedData {

    /**
     * Returns identifier of class of an element
     *
     * @return class id
     */
    public int getClassId();

    /**
     * Returns number of class that can return classifier of given element
     *
     * @return number of class
     */
    public int getClassNumber();

}

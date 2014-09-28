package com.ifmo.machinelearning.library;

/**
 * Abstract class that has the following methods:
 * {@link #getClassId()} and {@link #getClassId()}
 * <p>
 * Created by warrior on 19.09.14.
 */
public abstract class ClassifiedData {

    private int classId = -1;

    /**
     * Returns identifier of class of an element
     *
     * @return class id
     */
    public int getClassId() {
        return classId;
    }

    protected void setClassId(int classId) {
        this.classId = classId;
    }

    /**
     * Returns number of class that can return classifier of given element
     *
     * @return number of class
     */
    public abstract int getClassNumber();

}

package com.ifmo.machinelearning.library.core;

/**
 * Created by warrior on 05.12.14.
 */
public class ClassifiedInstanceDefaultImpl extends InstanceDefaultImpl implements ClassifiedInstance {

    protected final int classId;
    protected final int classNumber;

    public ClassifiedInstanceDefaultImpl(String[] attributeNames, double[] attributeValues, int classNumber, int classId) {
        super(attributeNames, attributeValues);
        this.classNumber = classNumber;
        this.classId = classId;
    }

    public ClassifiedInstanceDefaultImpl(String[] attributeNames, double[] attributeValues, int classNumber) {
        this(attributeNames, attributeValues, classNumber, -1);
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public int getClassNumber() {
        return classNumber;
    }
}

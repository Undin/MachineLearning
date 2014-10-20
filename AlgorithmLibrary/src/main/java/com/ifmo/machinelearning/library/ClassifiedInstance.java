package com.ifmo.machinelearning.library;

/**
 * Created by warrior on 20.10.14.
 */
public class ClassifiedInstance extends Instance implements ClassifiedData {

    protected final int classId;
    protected final int classNumber;

    public ClassifiedInstance(String[] attributeNames, double[] attributeValues, int classNumber, int classId) {
        super(attributeNames, attributeValues);
        this.classNumber = classNumber;
        this.classId = classId;
    }

    public ClassifiedInstance(String[] attributeNames, double[] attributeValues, int classNumber) {
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

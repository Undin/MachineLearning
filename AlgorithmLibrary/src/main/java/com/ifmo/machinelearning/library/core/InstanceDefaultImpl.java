package com.ifmo.machinelearning.library.core;

import java.util.Arrays;

/**
 * Created by warrior on 05.12.14.
 */
public class InstanceDefaultImpl implements Instance {

    protected final String[] attributeNames;
    protected final double[] attributeValues;

    public InstanceDefaultImpl(String[] attributeNames, double[] attributeValues) {
        if (attributeNames == null || attributeValues == null) {
            throw new IllegalArgumentException("arguments must be not null");
        }
        if (attributeNames.length != attributeValues.length) {
            throw new IllegalArgumentException("attributeNames.length != attributeValues.length");
        }
        this.attributeNames = attributeNames;
        this.attributeValues = attributeValues;
    }

    public int getAttributeNumber() {
        return attributeNames.length;
    }

    public String getAttributeName(int i) {
        return attributeNames[i];
    }

    public double getAttributeValue(int i) {
        return attributeValues[i];
    }

    public double[] getValues() {
        double[] copyValues = new double[attributeValues.length];
        System.arraycopy(attributeValues, 0, copyValues, 0, attributeValues.length);
        return copyValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceDefaultImpl instance = (InstanceDefaultImpl) o;

        if (!Arrays.equals(attributeNames, instance.attributeNames)) return false;
        if (!Arrays.equals(attributeValues, instance.attributeValues)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(attributeNames);
        result = 31 * result + Arrays.hashCode(attributeValues);
        return result;
    }
}


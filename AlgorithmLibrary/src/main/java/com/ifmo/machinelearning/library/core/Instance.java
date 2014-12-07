package com.ifmo.machinelearning.library.core;

/**
 * Created by warrior on 05.12.14.
 */
public interface Instance {

    public int getAttributeNumber();
    public String getAttributeName(int i);
    public double getAttributeValue(int i);
    public double[] getValues();

}

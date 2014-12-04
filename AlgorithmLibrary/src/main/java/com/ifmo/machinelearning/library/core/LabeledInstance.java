package com.ifmo.machinelearning.library.core;

/**
 * Created by warrior on 05.12.14.
 */
public interface LabeledInstance extends Instance {

    public double[] getLabels();
    public int getLabelNumber();
}

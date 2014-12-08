package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.List;

/**
 * Created by Whiplash on 02.12.2014.
 */
public interface QualityCriterion {

    static final double LOG2 = Math.log(2);

    public double getValue(List<List<ClassifiedInstance>> instances);

}

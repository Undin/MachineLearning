package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.List;

/**
 * Created by Whiplash on 02.12.2014.
 */
public interface QualityCriterion {

    public double getValue(List<List<ClassifiedInstance>> instances);

}

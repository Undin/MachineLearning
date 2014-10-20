package com.ifmo.machinelearning.library.classifiers;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.List;

/**
 * Created by warrior on 20.10.14.
 */
public abstract class AbstractInstanceClassifier extends AbstractClassifier<ClassifiedInstance> {

    public AbstractInstanceClassifier(List<ClassifiedInstance> data) {
        super(data);
    }
}

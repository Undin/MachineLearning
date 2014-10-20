package com.ifmo.machinelearning.library;

import java.util.List;

/**
 * Created by warrior on 20.10.14.
 */
public abstract class AbstractInstanceClassifier extends AbstractClassifier<ClassifiedInstance> {

    public AbstractInstanceClassifier(List<ClassifiedInstance> data) {
        super(data);
    }
}

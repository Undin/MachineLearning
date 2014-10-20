package com.ifmo.machinelearning.library.svm;

import com.ifmo.machinelearning.library.Instance;

/**
 * Created by Whiplash on 19.10.2014.
 */
public interface Kernel {

    public double eval(Instance first, Instance second);

}

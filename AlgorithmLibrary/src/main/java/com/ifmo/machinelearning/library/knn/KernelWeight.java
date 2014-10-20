package com.ifmo.machinelearning.library.knn;

import com.ifmo.machinelearning.library.Kernel;

/**
 * Created by Whiplash on 21.09.2014.
 */
public class KernelWeight implements Weight {

    private Kernel kernel;

    public KernelWeight(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public double weight(double distance) {
        return kernel.eval(distance);
    }

}

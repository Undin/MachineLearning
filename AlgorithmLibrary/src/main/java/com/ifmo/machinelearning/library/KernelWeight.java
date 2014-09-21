package com.ifmo.machinelearning.library;

/**
 * Weight function: {@code K(distance)}, where K - kernel function
 * <p>
 * Created by Whiplash on 21.09.2014.
 */
public class KernelWeight implements Weight {

    private static KernelWeight instance;
    /**
     * Kernel function
     */
    private Kernel kernel;

    /**
     * Returns the {@code DistanceWeight} object associated with the current Java application.
     * Most of the methods of class {@code DistanceWeight} are instance
     * methods and must be invoked with respect to the current runtime object.
     *
     * @param kernel {@link #kernel}
     * @return {@code DistanceWeight} object associated with the current
     * Java application.
     */
    public static KernelWeight getInstance(Kernel kernel) {
        if (instance == null) {
            instance = new KernelWeight(kernel);
        }
        return instance;
    }

    private KernelWeight(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public double weight(double distance) {
        return kernel.eval(distance);
    }

}

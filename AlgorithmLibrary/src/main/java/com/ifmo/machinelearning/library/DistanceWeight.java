package com.ifmo.machinelearning.library;

/**
 * Weight function: {@code 1/distance}
 * <p>
 * Created by Whiplash on 19.09.2014.
 */
public class DistanceWeight implements Weight {

    private static DistanceWeight instance;

    /**
     * Returns the {@code DistanceWeight} object associated with the current Java application.
     * Most of the methods of class {@code DistanceWeight} are instance
     * methods and must be invoked with respect to the current runtime object.
     *
     * @return {@code DistanceWeight} object associated with the current
     * Java application.
     */
    public static DistanceWeight getInstance() {
        if (instance == null) {
            instance = new DistanceWeight();
        }
        return instance;
    }

    private DistanceWeight() {
    }

    @Override
    public double weight(double distance) {
        return 1 / distance;
    }

}

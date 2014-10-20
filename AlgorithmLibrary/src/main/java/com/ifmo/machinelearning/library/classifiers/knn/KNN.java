package com.ifmo.machinelearning.library.classifiers.knn;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.Arrays;
import java.util.List;

/**
 * Realization of KNN algorithm
 * <p>
 * Created by Whiplash on 19.09.2014.
 *
 * @see <a href="http://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm">KNN</a>
 */
public class KNN extends AbstractInstanceClassifier {

    /**
     * Function of distance for type {@code T}
     */
    private Distance distanceFunction;
    /**
     * Function of weight for type {@code T}
     */
    private Weight weightFunction;
    /**
     * Number of nearest neighbor which uses to classifier element
     */
    private int k;

    /**
     * @param data
     * @param distanceFunction {@link #distanceFunction}
     * @param weightFunction   {@link #weightFunction}
     * @param k                {@link #k}
     */
    public KNN(List<ClassifiedInstance> data, Distance distanceFunction, Weight weightFunction, int k) {
        super(data);
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
        this.k = Math.min(k, size);
    }

    @Override
    protected void trainingInternal() {
    }

    @Override
    public int getSupposedClassId(ClassifiedInstance instance) {
        Integer[] ids = new Integer[size];
        double[] distances = new double[size];
        Arrays.setAll(ids, i -> i);
        Arrays.setAll(distances, i -> distanceFunction.distance(instance, get(i)));
        Arrays.sort(ids, (Integer o1, Integer o2) -> Double.compare(distances[o1], distances[o2]));

        double[] weights = new double[getClassNumber()];
        for (int i = 0; i < k; i++) {
            ClassifiedInstance neighbor = get(ids[i]);
            weights[neighbor.getClassId()] += weightFunction.weight(distances[ids[i]]);
        }

        return indexOfMaxElement(weights);
    }

    /**
     * Returns index of max element in {@code array}
     *
     * @param array array to be searched
     * @return index of max element in {@code array}
     */
    private int indexOfMaxElement(double[] array) {
        double maxValue = 0;
        int maxId = -1;
        for (int i = 0; i < array.length; i++) {
            if (maxValue < array[i]) {
                maxValue = array[i];
                maxId = i;
            }
        }
        return maxId;
    }
}

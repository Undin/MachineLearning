package com.ifmo.machinelearning.library.knn;

import com.ifmo.machinelearning.library.ClassifiedData;
import com.ifmo.machinelearning.library.Classifier;

import java.util.Arrays;
import java.util.List;

/**
 * Realization of KNN algorithm
 * <p>
 * Created by Whiplash on 19.09.2014.
 *
 * @see <a href="http://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm">KNN</a>
 */
public class KNN<T extends ClassifiedData> implements Classifier<T> {

    /**
     * Data necessary to classifier element which serves {@link #getSupposedClassId(ClassifiedData)}
     */
    private List<T> sample;
    /**
     * Function of distance for type {@code T}
     */
    private Distance<T, Double> distanceFunction;
    /**
     * Function of weight for type {@code T}
     */
    private Weight weightFunction;
    /**
     * Number of nearest neighbor which uses to classifier element
     */
    private int k;

    /**
     * @param sample           {@link #sample}
     * @param distanceFunction {@link #distanceFunction}
     * @param weightFunction   {@link #weightFunction}
     * @param k                {@link #k}
     */
    public KNN(List<T> sample, Distance<T, Double> distanceFunction, Weight weightFunction, int k) {
        this.sample = sample;
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
        this.k = Math.min(k, sample.size());
    }

    @Override
    public Classifier<T> training() {
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        Integer[] ids = new Integer[sample.size()];
        double[] distances = new double[sample.size()];
        Arrays.setAll(ids, i -> i);
        Arrays.setAll(distances, i -> distanceFunction.distance(t, sample.get(i)));
        Arrays.sort(ids, (Integer o1, Integer o2) -> Double.compare(distances[o1], distances[o2]));

        double[] weights = new double[t.getClassNumber()];
        for (int i = 0; i < k; i++) {
            T neighbor = sample.get(ids[i]);
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

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
public class KNN<T extends ClassifiedData> extends Classifier<T> {

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
     * @param data
     * @param distanceFunction {@link #distanceFunction}
     * @param weightFunction   {@link #weightFunction}
     * @param k                {@link #k}
     */
    public KNN(List<T> data, Distance<T, Double> distanceFunction, Weight weightFunction, int k) {
        super(data);
        this.distanceFunction = distanceFunction;
        this.weightFunction = weightFunction;
        this.k = Math.min(k, data.size());
    }

    @Override
    public Classifier<T> training() {
        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        Integer[] ids = new Integer[getData().size()];
        double[] distances = new double[getData().size()];
        Arrays.setAll(ids, i -> i);
        Arrays.setAll(distances, i -> distanceFunction.distance(t, getData().get(i)));
        Arrays.sort(ids, (Integer o1, Integer o2) -> Double.compare(distances[o1], distances[o2]));

        double[] weights = new double[getClassNumber()];
        for (int i = 0; i < k; i++) {
            T neighbor = getData().get(ids[i]);
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

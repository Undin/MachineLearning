package com.ifmo.machinelearning.homework5;


/**
 * Created by warrior on 16.11.14.
 */
public interface RecommenderSystem {

    public void train();

    public int getRating(long user, long item);

}

package com.ifmo.machinelearning.homework5;

import java.util.Arrays;

import static java.lang.Math.pow;
import static java.lang.Math.random;

/**
 * Created by Whiplash on 18.11.2014.
 */
public class BaselinePredictors implements RecommenderSystem {

    private final int users;
    private final int items;

    private double mu;
    private double lambda;
    private double gamma;

    private double[] userB;
    private double[] itemB;
    private byte[][] ratings;

    private double[][] p;
    private double[][] q;
    private int size;

    public BaselinePredictors(byte[][] ratings, int size) {
        this.ratings = ratings;
        this.size = size;
        users = ratings.length;
        items = ratings[0].length;
        userB = new double[users];
        itemB = new double[items];
        p = new double[users][size];
        q = new double[items][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < users; j++) {
                p[j][i] = random();
            }
            for (int j = 0; j < items; j++) {
                q[j][i] = random();
            }
        }
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public void train() {
        for (int j = 0; j < items; j++) {
            double itemRating = 0;
            double count = 0;
            for (int i = 0; i < users; i++) {
                if (ratings[i][j] != 0) {
                    itemRating += ratings[i][j];
                    count++;
                }
            }
            mu += (itemRating / count);
        }
        mu /= items;

        Arrays.setAll(itemB, this::initItem);
        Arrays.setAll(userB, this::initUser);

        int k = 0;
        double prev = 0;
        while (true) {
            for (int i = 0; i < users; i++) {
                for (int j = 0; j < items; j++) {
                    if (ratings[i][j] != 0) {
                        iter(i, j);
                    }
                }
            }
            k++;

            double res = getFunction();
            System.out.println("iter " + k + ": " + res);
            if (Math.abs(res - prev) < 1e2) {
                System.out.println("STOP TRAINING");
                return;
            }
            prev = res;
        }
    }

    public double getFunction() {
        double res = 0;
        for (int i = 0; i < users; i++) {
            for (int j = 0; j < items; j++) {
                if (ratings[i][j] != 0) {
                    res += pow(getE(i, j) - getPQ(i, j), 2) + lambda * (pow(itemB[j], 2) + pow(userB[i], 2) + getSqrNorm(q[j]) + getSqrNorm(p[i]));
                }
            }
        }
        return res;
    }

    private double getSqrNorm(double[] vec) {
        double res = 0;
        for (double i : vec) {
            res += pow(i, 2);
        }
        return res;
    }

    public double initItem(int item) {
        double up = 0;
        double count = 0;
        for (int user = 0; user < users; user++) {
            if (ratings[user][item] != 0) {
                up += (ratings[user][item] - mu);
                count++;
            }
        }
        return up / (25 + count);
    }

    public double initUser(int user) {
        double up = 0;
        double count = 0;
        for (int item = 0; item < items; item++) {
            if (ratings[user][item] != 0) {
                up += (ratings[user][item] - mu - itemB[item]);
                count++;
            }
        }
        return up / (10 + count);
    }

    private void iter(int userId, int itemId) {
        userB[userId] = userB[userId] + gamma * (getE(userId, itemId) - lambda * userB[userId]);
        itemB[itemId] = itemB[itemId] + gamma * (getE(userId, itemId) - lambda * itemB[itemId]);
        for (int i = 0; i < size; i++) {
            q[itemId][i] = q[itemId][i] + gamma * (getE(userId, itemId) * p[userId][i] - lambda * q[itemId][i]);
            p[userId][i] = p[userId][i] + gamma * (getE(userId, itemId) * q[itemId][i] - lambda * p[userId][i]);
        }
    }

    private double getPQ(int userId, int itemId) {
        double res = 0;
        if (userId != IdConverter.NO_ENTRY_VALUE && itemId != IdConverter.NO_ENTRY_VALUE) {
            for (int i = 0; i < size; i++) {
                res += p[userId][i] * q[itemId][i];
            }
        }
        return res;
    }

    private double getE(int userId, int itemId) {
        return ratings[userId][itemId] - mu - userB[userId] - itemB[itemId];
    }

    @Override
    public int getRating(long user, long item) {
        int uid = IdConverter.getUserId(user);
        int iid = IdConverter.getItemId(item);
        double bu = uid == IdConverter.NO_ENTRY_VALUE ? 0 : userB[uid];
        double bi = iid == IdConverter.NO_ENTRY_VALUE ? 0 : itemB[iid];
        return (int) (mu + bu + bi + getPQ(uid, iid) + .5);
    }
}

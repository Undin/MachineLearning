package com.ifmo.machinelearning.homework5;

import static java.lang.Math.pow;
import static java.lang.Math.random;

/**
 * Created by Whiplash on 18.11.2014.
 */
public class SVDSystem implements RecommenderSystem {

    private final int users;
    private final int items;

    private double mu;
    private double lambda;
    private double gamma;

    private double[] userB;
    private double[] itemB;
    private byte[][] ratings; // users x items

    private double[][] p; // users x size
    private double[][] q; // items x size
    private int size;

    public SVDSystem(byte[][] ratings, int size) {
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

//        Arrays.setAll(itemB, this::initItem);
//        Arrays.setAll(userB, this::initUser);

        double prev = 0;
        while (true) {
            for (int i = 0; i < users; i++) {
                for (int j = 0; j < items; j++) {
                    if (ratings[i][j] != 0) {
                        iter(i, j);
                    }
                }
            }
            double errorValue = evaluate();
            if (Math.abs(errorValue - prev) < 100) {
                return;
            }
            prev = errorValue;
        }
    }

    public double evaluate() {
        double error = 0;
        for (int i = 0; i < users; i++) {
            for (int j = 0; j < items; j++) {
                if (ratings[i][j] != 0) {
                    error += pow(getE(i, j), 2);
                }
            }
        }
        for (double[] w : p) {
            error += lambda * getSqrNorm(w);
        }
        for (double[] w : q) {
            error += lambda * getSqrNorm(w);
        }
        error += lambda * (getSqrNorm(itemB) + getSqrNorm(userB));
        return error;
    }

    private static double getSqrNorm(double[] vec) {
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
        double eui = getE(userId, itemId);
        userB[userId] = userB[userId] + gamma * (eui - lambda * userB[userId]);
        itemB[itemId] = itemB[itemId] + gamma * (eui - lambda * itemB[itemId]);
        for (int i = 0; i < size; i++) {
            p[userId][i] = p[userId][i] + gamma * (eui * q[itemId][i] - lambda * p[userId][i]);
            q[itemId][i] = q[itemId][i] + gamma * (eui * p[userId][i] - lambda * q[itemId][i]);
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
        return ratings[userId][itemId] - mu - userB[userId] - itemB[itemId] - getPQ(userId, itemId);
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

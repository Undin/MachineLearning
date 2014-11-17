package com.ifmo.machinelearning.homework5;

import java.util.*;

/**
 * Created by warrior on 16.11.14.
 */
public class KNNSystem implements RecommenderSystem {

    public static enum DistType {
        COS,
        PC,
        AC
    }

    private static final int DEFAULT_K = 5;

    private final byte[][] ratings;
    private final DistType distType;

    private final int userNumber;
    private final int itemNumber;

    private final double[] averageItemRatings;
    private final double[] averageUserRatings;
    private final double[][] distances;
    private final double[] sums;

    private double averageItemsRating;

    private List<Integer> indexes;
    private int k = DEFAULT_K;

    public KNNSystem(byte[][] ratings, DistType type) {
        this.ratings = ratings;
        this.distType = type;
        this.userNumber = ratings.length;
        this.itemNumber = ratings[0].length;
        this.averageItemRatings = new double[itemNumber];
        this.averageUserRatings = new double[userNumber];
        this.distances = new double[userNumber][userNumber];
        this.sums = new double[userNumber];
        this.indexes = new ArrayList<>(userNumber);
        for (int i = 0; i < userNumber; i++) {
            Arrays.fill(distances[i], -1);
        }
        for (int i = 0; i < userNumber; i++) {
            indexes.add(i);
        }
        int[] notNullItemMark = new int[itemNumber];
        for (int u = 0; u < userNumber; u++) {
            int notNullNumber = 0;
            for (int i = 0; i < itemNumber; i++) {
                byte r = ratings[u][i];
                if (r > 0) {
                    notNullNumber++;
                    notNullItemMark[i]++;
                    averageUserRatings[u] += r;
                    averageItemRatings[i] += r;
                }
            }
            averageUserRatings[u] /= notNullNumber;
        }
        for (int i = 0; i < itemNumber; i++) {
            averageItemRatings[i] /= notNullItemMark[i];
            averageItemsRating += averageItemRatings[i];
        }
        averageItemsRating /= itemNumber;

        for (int u = 0; u < userNumber; u++) {
            for (int i = 0; i < itemNumber; i++) {
                sums[u] += sqr(factor(u, i));
            }
        }
    }

    @Override
    public void train() {
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public int getRating(long user, long item) {
        int uid = IdConverter.getUserId(user);
        int iid = IdConverter.getItemId(item);
        double rating;
        if (uid == IdConverter.NO_ENTRY_VALUE) {
            rating = iid == IdConverter.NO_ENTRY_VALUE ? averageItemsRating : averageItemRatings[iid];
        } else {
            if (iid == IdConverter.NO_ENTRY_VALUE) {
                rating = averageUserRatings[uid];
            } else {
                rating = knn(uid, iid);
            }
        }
        return (int) (rating + 0.5);
    }

    private double distance(int u, int v) {
        if (distances[u][v] < 0) {
            double sum = 0;
            for (int i = 0; i < itemNumber; i++) {
                sum += factor(u, i) * factor(v, i);
            }
            distances[u][v] = distances[v][u] = sum / (Math.sqrt(sums[u]) * Math.sqrt(sums[v]));
        }
        return distances[u][v];
    }

    private double factor(int u, int i) {
        return factor(u, i, distType);
    }

    private double factor(int u, int i, DistType type) {
        switch (type) {
            case COS:
                return ratings[u][i];
            case PC:
                return ratings[u][i] > 0 ? ratings[u][i] - averageUserRatings[u] : 0;
            case AC:
                return ratings[u][i] > 0 ? ratings[u][i] - averageItemRatings[i] : 0;
            default:
                throw new IllegalStateException();
        }
    }

    private double knn(int user, int item) {
        double sumW = 0;
        double sumWR = 0;
        final double[] dist = new double[userNumber];
        for (int i = 0; i < userNumber; i++) {
            dist[i] = ratings[i][item] > 0 ? distance(user, i) : -1;
        }
        Collections.sort(indexes, (o1, o2) -> Double.compare(dist[o2], dist[o1]));
        int i = 0;
        int remaining = k;
        while (i < userNumber && remaining > 0) {
            double w = dist[indexes.get(i)];
            if (w > 0) {
                sumW += w;
                sumWR += w * meanCentering(ratings[indexes.get(i)][item]);
                remaining--;
            }
            i++;
        }
        return invMeaCentering(sumW == 0 ? 0 : sumWR / sumW);
    }

    private double meanCentering(double r) {
        return r - averageItemsRating;
    }

    private double invMeaCentering(double r) {
        return r + averageItemsRating;
    }

    private static double sqr(double a) {
        return a * a;
    }

}

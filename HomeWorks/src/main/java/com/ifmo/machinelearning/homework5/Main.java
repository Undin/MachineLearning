package com.ifmo.machinelearning.homework5;

import gnu.trove.list.TByteList;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TLongArrayList;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by warrior on 16.11.14.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File trainSet = new File("/Users/warrior/Programming/MachineLearning/HomeWorks/res/homework5/train.csv");
        BufferedReader reader = new BufferedReader(new FileReader(trainSet));
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            IdConverter.fromUserRealId(Long.parseLong(tokenizer.nextToken()));
            IdConverter.fromItemRealId(Long.parseLong(tokenizer.nextToken()));
        }
        reader.close();
        reader = new BufferedReader(new FileReader(trainSet));
        reader.readLine();
        byte[][] ratings = new byte[IdConverter.userNumber()][IdConverter.itemNumber()];
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            int user = IdConverter.fromUserRealId(Long.parseLong(tokenizer.nextToken()));
            int item = IdConverter.fromItemRealId(Long.parseLong(tokenizer.nextToken()));
            ratings[user][item] = Byte.parseByte(tokenizer.nextToken());
        }
        KNNSystem recommenderSystem = new KNNSystem(ratings);
        recommenderSystem.train();

        TLongList users = new TLongArrayList();
        TLongList items = new TLongArrayList();
        TByteList expectedRatings = new TByteArrayList();
        File testSet = new File("/Users/warrior/Programming/MachineLearning/HomeWorks/res/homework5/validation.csv");
        reader = new BufferedReader(new FileReader(testSet));
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            users.add(Long.parseLong(tokenizer.nextToken()));
            items.add(Long.parseLong(tokenizer.nextToken()));
            expectedRatings.add(Byte.parseByte(tokenizer.nextToken()));
        }

        double bestRmse = 100;
        double bestK = -1;
        KNNSystem.DistType bestType = KNNSystem.DistType.COS;
        int n = users.size();
        recommenderSystem.setDistType(KNNSystem.DistType.AC);
        for (int i = 0; i < 3; i++) {
            KNNSystem.DistType type = KNNSystem.DistType.values()[i];
            recommenderSystem.setDistType(type);
            for (int k = 2; k < 100; k++) {
                recommenderSystem.setK(k);
                double rmse = 0;
                for (int j = 0; j < n; j++) {
                    double predictValue = recommenderSystem.getRating(users.get(j), items.get(j));
                    int expectedValue = expectedRatings.get(j);
                    rmse += (expectedValue - predictValue) * (expectedValue - predictValue);
                }
                rmse = Math.sqrt(rmse / n);
                System.out.println("rmse: " + rmse + ", type: " + type + ", k: " + k);
                if (rmse < bestRmse) {
                    bestRmse = rmse;
                    bestK = k;
                    bestType = type;
                }
            }
        }
        System.out.println("bestRmse: " + bestRmse + ", bestType: " + bestType + ", k: " + bestK);
    }
}

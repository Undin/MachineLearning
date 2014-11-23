package com.ifmo.machinelearning.homework5;

import gnu.trove.list.TByteList;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TLongArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by warrior on 16.11.14.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        File trainSet = new File("./HomeWorks/res/homework5/train.csv");
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
//        KNNSystem recommenderSystem = new KNNSystem(ratings, KNNSystem.DistType.AC);
//        recommenderSystem.train();

        TLongList users = new TLongArrayList();
        TLongList items = new TLongArrayList();
        TByteList expectedRatings = new TByteArrayList();
        File testSet = new File("./HomeWorks/res/homework5/validation.csv");
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
        int n = users.size();
        for (int k = 10; k < 50; k++) {
            BaselinePredictors recommenderSystem = new BaselinePredictors(ratings, k);
            recommenderSystem.setGamma(0.005);
            recommenderSystem.setLambda(0.02);
            recommenderSystem.train();
            double rmse = 0;
            for (int j = 0; j < n; j++) {
                double predictValue = recommenderSystem.getRating(users.get(j), items.get(j));
                int expectedValue = expectedRatings.get(j);
                rmse += (expectedValue - predictValue) * (expectedValue - predictValue);
            }
            rmse = Math.sqrt(rmse / n);
            System.out.println("rmse: " + rmse + ", k: " + k);
            if (rmse < bestRmse) {
                bestRmse = rmse;
                bestK = k;
            }
        }
        System.out.println("bestRmse: " + bestRmse + ", k: " + bestK);
    }
}

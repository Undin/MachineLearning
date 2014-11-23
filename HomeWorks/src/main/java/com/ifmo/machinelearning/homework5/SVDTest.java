package com.ifmo.machinelearning.homework5;

import gnu.trove.list.TByteList;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TLongArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by warrior on 16.11.14.
 */
public class SVDTest {

    private static final TLongList users = new TLongArrayList();
    private static final TLongList items = new TLongArrayList();
    private static final TByteList expectedRatings = new TByteArrayList();
    private static final List<Result> results = new ArrayList<>();

    private static byte[][] ratings;

    public static void main(String[] args) throws IOException, InterruptedException {
        File[] files = new File[]{new File("./HomeWorks/res/homework5/train.csv"), new File("./HomeWorks/res/homework5/validation.csv")};
        String line;
        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                IdConverter.fromUserRealId(Long.parseLong(tokenizer.nextToken()));
                IdConverter.fromItemRealId(Long.parseLong(tokenizer.nextToken()));
            }
            reader.close();
        }
        ratings = new byte[IdConverter.userNumber()][IdConverter.itemNumber()];
        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                int user = IdConverter.fromUserRealId(Long.parseLong(tokenizer.nextToken()));
                int item = IdConverter.fromItemRealId(Long.parseLong(tokenizer.nextToken()));
                ratings[user][item] = Byte.parseByte(tokenizer.nextToken());
            }
        }

        File testSet = new File("./HomeWorks/res/homework5/validation.csv");
        BufferedReader reader = new BufferedReader(new FileReader(testSet));
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            users.add(Long.parseLong(tokenizer.nextToken()));
            items.add(Long.parseLong(tokenizer.nextToken()));
            expectedRatings.add(Byte.parseByte(tokenizer.nextToken()));
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        CountDownLatch latch = new CountDownLatch(500);
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                for (int k = 1; k < 6; k++) {
                    executor.submit(new Task(latch, 0.001 * i, 0.01 * j, k));
                }
            }
        }

        latch.await();

        double bestRmse = Double.MAX_VALUE;
        Result result = null;

        for (Result r : results) {
            if (r.rmse < bestRmse) {
                result = r;
            }
        }

        System.out.println(result);
    }


    private static class Task implements Runnable {

        private final CountDownLatch latch;

        private final double gamma;
        private final double lambda;
        private final int size;

        public Task(CountDownLatch latch, double gamma, double lambda, int size) {
            this.latch = latch;
            this.gamma = gamma;
            this.lambda = lambda;
            this.size = size;
        }

        @Override
        public void run() {
            SVDSystem recommenderSystem = new SVDSystem(ratings, size);
            recommenderSystem.setGamma(gamma);
            recommenderSystem.setLambda(lambda);
            recommenderSystem.train();
            double rmse = 0;

            int n = users.size();
            for (int j = 0; j < n; j++) {
                double predictValue = recommenderSystem.getRating(users.get(j), items.get(j));
                int expectedValue = expectedRatings.get(j);
                rmse += (expectedValue - predictValue) * (expectedValue - predictValue);
            }
            rmse = Math.sqrt(rmse / n);

            synchronized (results) {
                Result result = new Result(rmse, gamma, lambda, size);
                System.out.println(result);
                results.add(result);
            }
            latch.countDown();
        }
    }

    private static class Result {
        public final double rmse;
        public final double gamma;
        public final double lambda;
        public final int size;

        public Result(double rmse, double gamma, double lambda, int size) {
            this.rmse = rmse;
            this.gamma = gamma;
            this.lambda = lambda;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "rmse=" + rmse +
                    ", gamma=" + gamma +
                    ", lambda=" + lambda +
                    ", size=" + size +
                    '}';
        }
    }
}

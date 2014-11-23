package com.ifmo.machinelearning.homework5;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by warrior on 24.11.14.
 */
public class SVDMain {

    private static final double GAMMA = 0.01;
    private static final double LAMBDA = 0.01;
    private static final int SIZE = 10;

    public static void main(String[] args) throws IOException {
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
        byte[][] ratings = new byte[IdConverter.userNumber()][IdConverter.itemNumber()];
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

        SVDSystem recommenderSystem = new SVDSystem(ratings, SIZE);
        recommenderSystem.setGamma(GAMMA);
        recommenderSystem.setLambda(LAMBDA);
        recommenderSystem.train();

        File testSet = new File("./HomeWorks/res/homework5/test-ids.csv");
        File output = new File("./HomeWorks/res/homework5/svd-" + SIZE + "-" + GAMMA + "-" + LAMBDA + ".csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(testSet));
             PrintWriter writer = new PrintWriter(output)) {
            writer.println("id,rating");
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                int id = Integer.parseInt(tokenizer.nextToken());
                long user = Long.parseLong(tokenizer.nextToken());
                long item = Long.parseLong(tokenizer.nextToken());
                int r = recommenderSystem.getRating(user, item);
                writer.println(id + "," + r);
            }
        }
    }
}

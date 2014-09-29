package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.bayes.BayesClassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by warrior on 29.09.14.
 */
public class NaiveBayesClassifier extends BayesClassifier<Message> {

    private HashMap<Integer, Double> spamWordsProbabilities = new HashMap<>();
    private HashMap<Integer, Double> legitWordsProbabilities = new HashMap<>();

    public NaiveBayesClassifier(List<Message> data) {
        super(data);
    }

    @Override
    protected void calculateCredibilityFunction() {
        List<Message> spamMessages = new ArrayList<>();
        List<Message> legitMessages = new ArrayList<>();
        for (Message message : getData()) {
            if (message.getClassId() == 0) {
                spamMessages.add(message);
            } else {
                legitMessages.add(message);
            }
        }
        countProbabilities(spamWordsProbabilities, spamMessages);
        countProbabilities(legitWordsProbabilities, legitMessages);
    }

    private void countProbabilities(HashMap<Integer, Double> probabilities, List<Message> messages) {
        int sum = 0;
        for (Message message : messages) {
            sum += message.getWords().size();
            for (Integer i : message.getWords()) {
                Double value;
                if ((value = probabilities.get(i)) != null) {
                    probabilities.put(i, value + 1);
                } else {
                    probabilities.put(i, 1D);
                }
            }
        }
        double lnSum = StrictMath.log(sum);
        for (Integer i : probabilities.keySet()) {
            Double value = probabilities.get(i);
            probabilities.put(i, StrictMath.log(value) - lnSum);
        }
    }

    @Override
    protected double credibilityFunctionLn(Message message, int classId) {
        HashMap<Integer, Double> probabilities = classId == 0 ? spamWordsProbabilities : legitWordsProbabilities;
        double probabilityLn = 0;
        for (Integer i : message.getWords()) {
            Double value = probabilities.get(i);
            if (value != null) {
                probabilityLn += value;
            }
        }
        return probabilityLn;
    }
}

package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.bayes.BayesClassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by warrior on 29.09.14.
 */
public class NaiveBayesClassifier extends BayesClassifier<Message> {

    private Map<Integer, Double> spamWordsProbabilities = new HashMap<>();
    private Map<Integer, Double> legitWordsProbabilities = new HashMap<>();

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
        Map<Integer, Double> count = new HashMap<>();
        calculateCount(count, getData());
        calculateCount(spamWordsProbabilities, spamMessages);
        calculateCount(legitWordsProbabilities, legitMessages);

        calculateProbabilityLn(count, spamWordsProbabilities);
        calculateProbabilityLn(count, legitWordsProbabilities);
    }

    private void calculateCount(Map<Integer, Double> probabilities, List<Message> messages) {
        for (Message message : messages) {
            for (Integer i : message.getWords()) {
                Double value = probabilities.getOrDefault(i, 0D);
                probabilities.put(i, value + 1);
            }
        }
    }

    private void calculateProbabilityLn(Map<Integer, Double> all, Map<Integer, Double> part) {
        for (Integer i : part.keySet()) {
            Double allCountLn = StrictMath.log(all.get(i));
            Double partCountLn = StrictMath.log(part.get(i));
            part.put(i, partCountLn - allCountLn);
        }
    }

    @Override
    protected double credibilityFunctionLn(Message message, int classId) {
        Map<Integer, Double> probabilities = classId == 0 ? spamWordsProbabilities : legitWordsProbabilities;
        double probabilityLn = 0;
        for (Integer i : message.getWords()) {
            probabilityLn += probabilities.getOrDefault(i, 0D);
        }
        return probabilityLn;
    }
}

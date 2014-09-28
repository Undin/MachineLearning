package com.ifmo.machinelearning.library.bayes;

import com.ifmo.machinelearning.library.Classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Bayes<T extends BayesElement> extends Classifier<T> {

    List<Double> classProbabilities;
    Map<Object, List<Double>> elementProbabilities;

    public Bayes(List<T> data) {
        super(data);
        classProbabilities = new ArrayList<>(getClassNumber());
        elementProbabilities = new HashMap<>();
    }

    @Override
    public Classifier<T> training() {
        Map<Object, List<Integer>> entries = new HashMap<>();
        List<Integer> numberOfRepresentatives = new ArrayList<>();
        for (int i = 0; i < getClassNumber(); i++) {
            numberOfRepresentatives.add(0);
        }
        for (T t : getData()) {
            numberOfRepresentatives.set(t.getClassId(), numberOfRepresentatives.get(t.getClassId()) + 1);
            for (Object element : t) {
                List<Integer> entriesList = entries.remove(t);
                if (entriesList == null) {
                    entriesList = new ArrayList<>(getClassNumber());
                    for (int i = 0; i < getClassNumber(); i++) {
                        entriesList.add(0);
                    }
                }
                entriesList.set(t.getClassId(), entriesList.get(t.getClassId()) + 1);
                entries.put(element, entriesList);
            }
        }

        for (int i = 0; i < getClassNumber(); i++) {
            classProbabilities.add(getProbability(numberOfRepresentatives, i));
        }
        for (Object element : entries.keySet()) {
            List<Double> listOfProbabilities = new ArrayList<>(getClassNumber());
            List<Integer> neededEntries = entries.get(element);
            for (int i = 0; i < getClassNumber(); i++) {
                listOfProbabilities.add(getProbability(neededEntries, i));
            }
            elementProbabilities.put(element, listOfProbabilities);
        }

        return this;
    }

    @Override
    public int getSupposedClassId(T t) {
        List<Double> resultProbabilities = new ArrayList<>(getClassNumber());
        for (int i = 0; i < getClassNumber(); i++) {
            resultProbabilities.add(classProbabilities.get(i));
        }
        for (Object element : t) {
            if (elementProbabilities.containsKey(element)) {
                for (int i = 0; i < getClassNumber(); i++) {
                    resultProbabilities.set(i, resultProbabilities.get(i) * elementProbabilities.get(element).get(i));
                }
            }
        }
        return indexOfMaxElement(resultProbabilities);
    }

    private double getProbability(List<Integer> entries, int classId) {
        double result = entries.get(classId);
        double entriesNumber = 0;
        for (Integer i : entries) {
            entriesNumber += i;
        }
        return result / entriesNumber * 0.998 + 0.001;
    }

    private int indexOfMaxElement(List<Double> array) {
        double maxValue = 0;
        int maxId = 0;
        for (int i = 0; i < array.size(); i++) {
            if (maxValue < array.get(i)) {
                maxValue = array.get(i);
                maxId = i;
            }
        }
        return maxId;
    }

}

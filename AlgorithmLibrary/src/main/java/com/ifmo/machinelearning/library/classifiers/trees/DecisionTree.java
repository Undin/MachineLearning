package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Whiplash on 02.12.2014.
 */
public class DecisionTree extends AbstractInstanceClassifier {

    private Tree root;
    private QualityCriterion criterion;
    private int stopSize;

    public DecisionTree(List<ClassifiedInstance> data, QualityCriterion criterion, int stopSize) {
        super(data);
        this.criterion = criterion;
        this.stopSize = stopSize;
    }

    @Override
    protected void trainInternal() {
        root = buildTree(getData());
        int i = 1;
        i++;
    }

    private Tree buildTree(List<ClassifiedInstance> sample) {
        boolean isLeaf = true;
        int classId = sample.get(0).getClassId();
        for (ClassifiedInstance instance : sample) {
            isLeaf &= (classId == instance.getClassId());
        }
        if (isLeaf || sample.size() <= stopSize) {
            return new Tree(getMostPopularClassId(sample));
        }

        int attributeNumber = sample.get(0).getAttributeNumber();
        double bestQuality = -1;
        Function<ClassifiedInstance, Integer> bestFunction = null;
        for (int i = 0; i < attributeNumber; i++) {
            int attribute = i;
            Set<Double> set = sample.stream().map(instance -> instance.getAttributeValue(attribute)).collect(Collectors.toSet());
            for (Double value : set) {
                Function<ClassifiedInstance, Integer> function = instance -> instance.getAttributeValue(attribute) > value ? 1 : 0;
                double quality = criterion.getValue(splitInstances(function, sample));
                if (bestQuality < quality) {
                    bestFunction = function;
                    bestQuality = quality;
                }
            }
        }
        List<List<ClassifiedInstance>> lists = splitInstances(bestFunction, sample);
        List<Tree> child = lists.stream().map(this::buildTree).collect(Collectors.toList());

        return new Tree(bestFunction, child);
    }

    private int getMostPopularClassId(List<ClassifiedInstance> instances) {
        Map<Integer, Integer> classIds = new HashMap<>(instances.get(0).getClassNumber());
        for (ClassifiedInstance instance : instances) {
            int id = instance.getClassId();
            classIds.put(id, classIds.getOrDefault(id, 0) + 1);
        }
        int bestId = 0;
        int bestEntries = -1;
        for (Integer classId : classIds.keySet()) {
            int entries = classIds.get(classId);
            if (entries > bestEntries) {
                bestEntries = entries;
                bestId = classId;
            }
        }
        return bestId;
    }

    private List<List<ClassifiedInstance>> splitInstances(Function<ClassifiedInstance, Integer> function,
                                                          List<ClassifiedInstance> sample) {
        List<List<ClassifiedInstance>> lists = new ArrayList<>(getClassNumber());
        for (int i = 0; i < getClassNumber(); i++) {
            lists.add(new ArrayList<>());
        }
        for (ClassifiedInstance instance : sample) {
            lists.get(function.apply(instance)).add(instance);
        }
        return lists;
    }

    @Override
    public int getSupposedClassId(ClassifiedInstance classifiedInstance) {
        Tree node = root;
        while (node.getClassId() == Tree.NO_ENTRY_VALUE) {
            node = node.getChild(node.eval(classifiedInstance));
        }
        return node.getClassId();
    }
}

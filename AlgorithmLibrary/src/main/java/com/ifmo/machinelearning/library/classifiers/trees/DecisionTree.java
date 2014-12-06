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
        return splitSample(sample);
    }

    private Tree splitSample(List<ClassifiedInstance> sample) {
        int attributeNumber = sample.get(0).getAttributeNumber();
        double bestQuality = -1;
        int bestAttributeTmp = 0;
        Function<ClassifiedInstance, Integer> bestFunction = null;
        for (int i = 0; i < attributeNumber; i++) {
            int attribute = i;
            double quality = criterion.getValue(splitInstances(classifiedInstance -> (int) classifiedInstance.getAttributeValue(attribute), sample));
            if (bestQuality < quality) {
                bestAttributeTmp = attribute;
                bestQuality = quality;
            }
        }
        int bestAttribute = bestAttributeTmp;
        Set<Double> set = sample.stream().map(instance -> instance.getAttributeValue(bestAttribute)).collect(Collectors.toSet());
        List<Double> values = new ArrayList<>(set);
        Collections.sort(values);
        bestQuality = 0;
        for (int j = 0; j < values.size() - 1; j++) {
            double value = (values.get(j + 1) - values.get(j)) / 2;
            Function<ClassifiedInstance, Integer> function = instance -> instance.getAttributeValue(bestAttribute) > value ? 1 : 0;
            double quality = criterion.getValue(splitInstances(function, sample));
            if (bestQuality < quality) {
                bestFunction = function;
                bestQuality = quality;
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
        Map<Integer, List<ClassifiedInstance>> map = new HashMap<>();
        List<List<ClassifiedInstance>> lists = new ArrayList<>(getClassNumber());
        for (ClassifiedInstance instance : sample) {
            Integer value = function.apply(instance);
            List<ClassifiedInstance> list = map.getOrDefault(value, new ArrayList<>());
            list.add(instance);
            map.put(value, list);
        }
        lists.addAll(map.keySet().stream().map(map::get).collect(Collectors.toList()));
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

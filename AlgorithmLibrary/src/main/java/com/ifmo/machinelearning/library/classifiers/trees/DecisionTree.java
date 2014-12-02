package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Whiplash on 02.12.2014.
 */
public class DecisionTree extends AbstractInstanceClassifier {

    private Tree root;
    private QualityCriterion criterion;

    public DecisionTree(List<ClassifiedInstance> data, QualityCriterion criterion) {
        super(data);
        this.criterion = criterion;
    }

    @Override
    protected void trainInternal() {

    }

    private Tree buildTree(List<ClassifiedInstance> sample, Function<ClassifiedInstance, Integer> function) {
        boolean isLeaf = true;
        int classId = sample.get(0).getClassId();
        for (ClassifiedInstance instance : sample) {
            isLeaf &= (classId == instance.getClassId());
        }
        if (isLeaf) {
            return new Tree(classId);
        }
        return new Tree(function, splitTree(sample));
    }

    private List<Tree> splitTree(List<ClassifiedInstance> sample) {
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
        List<Tree> child = new ArrayList<>();
        for (List<ClassifiedInstance> list : lists) {
            child.add(buildTree(list, bestFunction));
        }
        return child;
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
        return 0;
    }
}

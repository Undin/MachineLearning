package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Whiplash on 02.12.2014.
 */
public class Tree {

    public static final int NO_ENTRY_VALUE = Integer.MIN_VALUE;

    private List<Tree> child = new ArrayList<>();
    private int classId = NO_ENTRY_VALUE;
    private Function<ClassifiedInstance, Integer> splitFunction;

    public Tree(int classId) {
        this.classId = classId;
    }

    public Tree(Function<ClassifiedInstance, Integer> splitFunction, List<Tree> child) {
        this.splitFunction = splitFunction;
        this.child = child;
    }

    public int getClassId() {
        return classId;
    }

    public int size() {
        return child.size();
    }

    public Tree getChild(int i) {
        return child.get(i);
    }

    public int eval(ClassifiedInstance instance) {
        return splitFunction.apply(instance);
    }

    public void addChild(Tree tree) {
        child.add(tree);
    }

}

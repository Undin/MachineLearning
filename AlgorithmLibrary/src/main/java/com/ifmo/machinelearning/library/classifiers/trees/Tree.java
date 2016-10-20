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

    private List<Tree> children = new ArrayList<>();
    private Tree parent;
    private int classId = NO_ENTRY_VALUE;
    private Function<ClassifiedInstance, Integer> splitFunction;

    public Tree(int classId) {
        this.classId = classId;
    }

    public Tree(Function<ClassifiedInstance, Integer> splitFunction, List<Tree> children) {
        this.splitFunction = splitFunction;
        this.children = children;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public Tree getParent() {
        return parent;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public int getClassId() {
        return classId;
    }

    public int size() {
        return children.size();
    }

    public Tree getChild(int i) {
        return children.get(i);
    }

    public Tree setChild(int i, Tree newChild) {
        return children.set(i, newChild);
    }

    public int eval(ClassifiedInstance instance) {
        return splitFunction.apply(instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tree tree = (Tree) o;

        if (classId != tree.classId) return false;
        if (children != null ? !children.equals(tree.children) : tree.children != null) return false;
        if (parent != null ? !parent.equals(tree.parent) : tree.parent != null) return false;
        if (splitFunction != null ? !splitFunction.equals(tree.splitFunction) : tree.splitFunction != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = children != null ? children.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + classId;
        result = 31 * result + (splitFunction != null ? splitFunction.hashCode() : 0);
        return result;
    }
}

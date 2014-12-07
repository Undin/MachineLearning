package com.ifmo.machinelearning.library.classifiers.trees;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Whiplash on 02.12.2014.
 */
public class GainRatio implements QualityCriterion {

    @Override
    public double getValue(List<List<ClassifiedInstance>> instances) {
        List<ClassifiedInstance> all = new ArrayList<>();
        instances.forEach(all::addAll);
        double info = information(all);
        double splitInfo = 0;
        for (List<ClassifiedInstance> list : instances) {
            double tmp = ((double) list.size()) / all.size();
            info -= (tmp * information(list));
            splitInfo += tmp * Math.log(tmp) / LOG2;
        }
        return info / (-splitInfo);
    }

    private double information(List<ClassifiedInstance> instances) {
        if (instances.isEmpty()) {
            return -1;
        }
        Map<Integer, Double> p = new HashMap<>(instances.get(0).getClassNumber());
        for (ClassifiedInstance instance : instances) {
            int id = instance.getClassId();
            p.put(id, p.getOrDefault(id, 0.) + 1);
        }
        int size = instances.size();
        double res = 0.;
        for (Integer id : p.keySet()) {
            double pValue = p.get(id) / size;
            res += Math.log(pValue) / LOG2 * pValue;
        }
        return -res;
    }
}

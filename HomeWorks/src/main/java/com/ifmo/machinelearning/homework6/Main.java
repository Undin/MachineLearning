package com.ifmo.machinelearning.homework6;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;
import com.ifmo.machinelearning.library.test.Statistics;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Whiplash on 01.12.2014.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<ClassifiedInstance> train = InstanceCreator.classifiedInstancesFromFile("HomeWorks/res/homework6/train");
        List<ClassifiedInstance> test = InstanceCreator.classifiedInstancesFromFile("HomeWorks/res/homework6/valid");

//        Collections.shuffle(train);
        DecisionTreeTestMachine testMachine = new DecisionTreeTestMachine(train);
        for (int i = 1; i < train.size(); i++) {
            testMachine.setSize(i);
            Statistics statistics = testMachine.test(test);
            System.out.println("Size: " + i + "\nF-measure: " + statistics.getFMeasure());
        }
    }


}

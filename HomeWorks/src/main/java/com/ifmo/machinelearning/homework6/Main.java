package com.ifmo.machinelearning.homework6;

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier;
import com.ifmo.machinelearning.library.classifiers.trees.DecisionTree;
import com.ifmo.machinelearning.library.classifiers.trees.IGain;
import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;

import java.io.IOException;
import java.util.List;

/**
 * Created by Whiplash on 01.12.2014.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<ClassifiedInstance> train = InstanceCreator.classifiedInstancesFromFile("HomeWorks/res/homework6/train");
        List<ClassifiedInstance> test = InstanceCreator.classifiedInstancesFromFile("HomeWorks/res/homework6/valid");

        for (int i = 3; i < 4; i++) {
            int count = 0;
            AbstractInstanceClassifier classifier = new DecisionTree(train, IGain.getInstance(), i);
            classifier.train();
            for (ClassifiedInstance instance : test) {
                if (instance.getClassId() != classifier.getSupposedClassId(instance)) {
                    count++;
                }
            }
            System.out.println(String.format("I: %d, Error: %d", i, count));
        }
    }

    /*
    File fileData = new File("HomeWorks/res/homework6/arcene_valid.data");
        File fileLabels = new File("HomeWorks/res/homework6/arcene_valid.labels");
        BufferedReader bf = new BufferedReader(new FileReader(fileData));
        String str;
        List<List<Integer>> datas = new ArrayList<>();
        while ((str = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(str);
            List<Integer> row = new ArrayList<>();
            while (st.hasMoreTokens()) {
                row.add(Integer.parseInt(st.nextToken()));
            }
            datas.add(row);
        }
        bf.close();

        PrintWriter pw = new PrintWriter("HomeWorks/res/homework6/valid");
        for (int i = 0; i < datas.get(0).size(); i++) {
            pw.println(String.format("@attr attr%d", i));
        }
        pw.println("@class 2");
        pw.println();

        bf = new BufferedReader(new FileReader(fileLabels));
        for (List<Integer> list : datas) {
            for (Integer val : list) {
                pw.print(val + " ");
            }
            pw.println(bf.readLine());
        }
        bf.close();
        pw.close();
     */

}

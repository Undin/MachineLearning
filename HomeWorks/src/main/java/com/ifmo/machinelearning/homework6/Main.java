package com.ifmo.machinelearning.homework6;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;
import com.ifmo.machinelearning.library.core.InstanceCreator;

import java.io.IOException;
import java.util.List;

/**
 * Created by Whiplash on 01.12.2014.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<ClassifiedInstance> sample = InstanceCreator.classifiedInstancesFromFile("./HomeWorks/res/homework6/train");
        for (ClassifiedInstance instance : sample) {
            System.out.println(instance.getClassId());
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

package com.ifmo.machinelearning.library;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by warrior on 10.10.14.
 */
public class InstanceCreator {

    private static final String ATTR = "@attr";
    private static final String CLASS = "@class";

    private InstanceCreator() {
    }

    public static List<ClassifiedInstance> classifiedInstancesFromFile(String fileName) {
        List<Instance> instances = instancesFromFile(fileName);
        List<ClassifiedInstance> classifiedInstances = new ArrayList<>(instances.size());
        for (Instance instance : instances) {
            classifiedInstances.add((ClassifiedInstance) instance);
        }
        return classifiedInstances;
    }

    public static List<Instance> instancesFromFile(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String line;
        boolean readInstances = false;
        int classNumber = 0;
        String[] attributeNames = null;
        List<String> attributeNameList = new ArrayList<>();
        List<Instance> data = null;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (!readInstances) {
                        if (line.startsWith("@")) {
                            StringTokenizer tokenizer = new StringTokenizer(line);
                            String mark = tokenizer.nextToken();
                            switch (mark) {
                                case ATTR:
                                    attributeNameList.add(tokenizer.nextToken());
                                    continue;
                                case CLASS:
                                    classNumber = Integer.parseInt(tokenizer.nextToken());
                                    continue;
                            }
                        } else {
                            readInstances = true;
                            if (attributeNameList.isEmpty()) {
                                return null;
                            }
                            data = new ArrayList<>();
                            attributeNames = attributeNameList.toArray(new String[attributeNameList.size()]);
                            data.add(readInstance(line, attributeNames, classNumber));
                        }
                    } else {
                        data.add(readInstance(line, attributeNames, classNumber));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    private static Instance readInstance(String line, String[] attributeNames, int classNumber) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",; \t");
        double[] values = new double[attributeNames.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(tokenizer.nextToken());
        }
        if (classNumber > 0) {
            int classId = Integer.parseInt(tokenizer.nextToken());
            return new ClassifiedInstance(attributeNames, values, classNumber, classId);
        } else {
            return new Instance(attributeNames, values);
        }
    }
}

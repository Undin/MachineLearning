package com.ifmo.machinelearning.library.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<ClassifiedInstanceImpl> classifiedInstancesFromFile(String fileName) {
        List<InstanceImpl> instances = instancesFromFile(fileName);
        List<ClassifiedInstanceImpl> classifiedInstances = new ArrayList<>(instances.size());
        for (InstanceImpl instance : instances) {
            classifiedInstances.add((ClassifiedInstanceImpl) instance);
        }
        return classifiedInstances;
    }

    public static List<InstanceImpl> instancesFromFile(String fileName) {
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
        List<InstanceImpl> data = null;
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

    private static InstanceImpl readInstance(String line, String[] attributeNames, int classNumber) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",; \t");
        double[] values = new double[attributeNames.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(tokenizer.nextToken());
        }
        if (classNumber > 0) {
            int classId = Integer.parseInt(tokenizer.nextToken());
            return new ClassifiedInstanceImpl(attributeNames, values, classNumber, classId);
        } else {
            return new InstanceImpl(attributeNames, values);
        }
    }

    private static class InstanceImpl implements Instance {

        protected final String[] attributeNames;
        protected final double[] attributeValues;

        public InstanceImpl(String[] attributeNames, double[] attributeValues) {
            if (attributeNames == null || attributeValues == null) {
                throw new IllegalArgumentException("arguments must be not null");
            }
            if (attributeNames.length != attributeValues.length) {
                throw new IllegalArgumentException("attributeNames.length != attributeValues.length");
            }
            this.attributeNames = attributeNames;
            this.attributeValues = attributeValues;
        }

        public int getAttributeNumber() {
            return attributeNames.length;
        }

        public String getAttributeName(int i) {
            return attributeNames[i];
        }

        public double getAttributeValue(int i) {
            return attributeValues[i];
        }

        public double[] getValues() {
            double[] copyValues = new double[attributeValues.length];
            System.arraycopy(attributeValues, 0, copyValues, 0, attributeValues.length);
            return copyValues;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InstanceImpl instance = (InstanceImpl) o;

            if (!Arrays.equals(attributeNames, instance.attributeNames)) return false;
            if (!Arrays.equals(attributeValues, instance.attributeValues)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(attributeNames);
            result = 31 * result + Arrays.hashCode(attributeValues);
            return result;
        }
    }

    private static class ClassifiedInstanceImpl extends InstanceImpl implements ClassifiedInstance {

        protected final int classId;
        protected final int classNumber;

        public ClassifiedInstanceImpl(String[] attributeNames, double[] attributeValues, int classNumber, int classId) {
            super(attributeNames, attributeValues);
            this.classNumber = classNumber;
            this.classId = classId;
        }

        public ClassifiedInstanceImpl(String[] attributeNames, double[] attributeValues, int classNumber) {
            this(attributeNames, attributeValues, classNumber, -1);
        }

        @Override
        public int getClassId() {
            return classId;
        }

        @Override
        public int getClassNumber() {
            return classNumber;
        }
    }
}

package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.ClassifiedData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Message implements ClassifiedData {

    private static final String SPAM_MARK = "spmsg";

    private String name;
    private List<Integer> words;
    private int classId = -1;

    public Message(File file) throws IOException {
        name = file.getName();
        words = new ArrayList<>();
        classId = name.contains(SPAM_MARK) ? 0 : 1;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileData = "";
        String line;
        while ((line = reader.readLine()) != null) {
            fileData += line;
        }

        StringTokenizer st = new StringTokenizer(fileData, "Subject: \r\n\t");
        while (st.hasMoreTokens()) {
            words.add(Integer.parseInt(st.nextToken()));
        }
    }

    public boolean contains(Integer word) {
        return words.contains(word);
    }

    public int size() {
        return words.size();
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public int getClassNumber() {
        return 2;
    }

}

package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.bayes.BayesElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Message extends BayesElement<Integer> {

    private static final String SPAM_MARK = "spmsg";

    private List<Integer> words;
    private int classId = -1;

    public Message(File file) throws IOException {
        words = new ArrayList<>();
        classId = file.getName().contains(SPAM_MARK) ? 0 : 1;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileData = "";
        String line;
        while ((line = reader.readLine()) != null) {
            fileData += line + "\n";
        }

        StringTokenizer st = new StringTokenizer(fileData, "Subject: \r\n\t");
        while (st.hasMoreTokens()) {
            words.add(Integer.parseInt(st.nextToken()));
        }
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public int getClassNumber() {
        return 2;
    }

    @Override
    public Iterator<Integer> iterator() {
        return words.iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        words.forEach(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return words.spliterator();
    }

}

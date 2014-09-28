package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.ClassifiedData;

import java.io.*;
import java.util.*;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Message extends ClassifiedData {

    private static final String SPAM_MARK = "spmsg";

    private List<Integer> words;

    public Message(List<Integer> words, int classId) {
        this.words = words;
        setClassId(classId);
    }

    public Message(List<Integer> words) {
        this(words, -1);
    }

    @Override
    public int getClassNumber() {
        return 2;
    }

    public static Message createMessage(File file) throws IOException {
        ArrayList<Integer> words = new ArrayList<>();
        int classId = file.getName().contains(SPAM_MARK) ? 0 : 1;

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
        return new Message(words, classId);
    }

}

package com.ifmo.machinelearning.homework2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Main {

    private static final File messageDirectory = new File("./Bayes/pu1");

    public static void main(String[] args) throws IOException {
        List<Message> messages = new ArrayList<>();
        for (File file : messageDirectory.listFiles()) {
            if (file.isFile()) {
                messages.add(new Message(file));
            }
        }
    }

}

package com.ifmo.machinelearning.homework2;

import com.ifmo.machinelearning.library.ClassifiedData;

/**
 * Created by Whiplash on 28.09.2014.
 */
public class Message implements ClassifiedData {

    @Override
    public int getClassId() {
        return 0;
    }

    @Override
    public int getClassNumber() {
        return 2;
    }

}

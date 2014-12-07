package com.ifmo.machinelearning.homework7;

import com.ifmo.machinelearning.library.core.ClassifiedInstance;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by warrior on 05.12.14.
 */
public class NumberImageInstance implements ClassifiedInstance {

    private final byte[] bytes;
    private final byte label;

    public NumberImageInstance(byte[] bytes, byte label) {
        this.bytes = bytes;
        this.label = label;
    }

    @Override
    public int getAttributeNumber() {
        return bytes.length;
    }

    @Override
    public String getAttributeName(int i) {
        if (i > 0 && i < 10) {
            return "attr" + i;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public double getAttributeValue(int i) {
        return (bytes[i] & 0xFF) / 255D;
    }

    @Override
    public double[] getValues() {
        double[] values = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            values[i] = (bytes[i] & 0xFF) / 255D;
        }
        return values;
    }

    @Override
    public int getClassId() {
        return label;
    }

    @Override
    public int getClassNumber() {
        return 10;
    }

    public static List<NumberImageInstance> createFromFiles(String data, String labels) {
        List<NumberImageInstance> instances = new ArrayList<>();
        try (DataInputStream imagesStream = new DataInputStream(new FileInputStream(data));
             DataInputStream labelsStream = new DataInputStream(new FileInputStream(labels))) {

            imagesStream.readInt();
            labelsStream.readInt();
            int imageNumber = imagesStream.readInt();
            int labelNumber = labelsStream.readInt();
            if (imageNumber != labelNumber) {
                throw new IllegalArgumentException("imageNumber != labelNumber");
            }
            int rows = imagesStream.readInt();
            int columns = imagesStream.readInt();
            for (int i = 0; i < imageNumber; i++) {
                byte[] bytes = new byte[rows * columns];
                imagesStream.readFully(bytes);
                byte label = labelsStream.readByte();
                instances.add(new NumberImageInstance(bytes, label));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return instances;
    }
}

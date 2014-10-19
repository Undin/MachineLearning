package com.ifmo.machinelearning.homework3;

import com.ifmo.machinelearning.Point;
import com.ifmo.machinelearning.library.Kernel2;

import java.util.function.Function;

/**
 * Created by Whiplash on 19.10.2014.
 */
public class FunctionKernel implements Kernel2<Point> {

    private static FunctionKernel instance;

    private Function<Point, Double> firstFunction;
    private Function<Point, Double> secondFunction;

    public static FunctionKernel getInstance(Function<Point, Double> firstFunction, Function<Point, Double> secondFunction) {
        if (instance == null) {
            instance = new FunctionKernel(firstFunction, secondFunction);
        }
        return instance;
    }

    private FunctionKernel(Function<Point, Double> firstFunction, Function<Point, Double> secondFunction) {
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    @Override
    public double eval(Point first, Point second) {
        return firstFunction.apply(first) * secondFunction.apply(second);
    }

}

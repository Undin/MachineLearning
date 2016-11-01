package com.ifmo.machinelearning.master_homework1

import weka.classifiers.AbstractClassifier
import weka.classifiers.bayes.NaiveBayes
import weka.classifiers.functions.LibSVM
import weka.classifiers.lazy.IBk
import weka.classifiers.trees.RandomForest
import weka.classifiers.trees.J48

/**
 * Created by warrior on 11/2/16.
 */
enum class FeatureRanker(val ranker: Ranker) {
    PEARSON(PearsonsCorrelationRanker()),
    SPEARMAN(SpearmansCorrelationRanker()),
    MUTUAL_INFORMATION(MutualInformationRanker()),
    RANDOM_FOREST(RandomForestRanker())
}

enum class Classifier(val classifier: AbstractClassifier) {
    KNN(IBk().apply { knn = 3 }),
    SVM(LibSVM()),
    NAIVE_BAYES(NaiveBayes()),
    DECISION_TREE(J48()),
    RANDOM_FOREST(RandomForest())
}

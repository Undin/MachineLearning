package com.ifmo.machinelearning.master_homework1

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ifmo.machinelearning.library.core.ClassifiedInstance
import com.ifmo.machinelearning.library.core.InstanceCreator
import com.ifmo.machinelearning.library.core.SubspaceClassifierInstance
import kotlinx.support.jdk8.collections.forEach
import weka.classifiers.AbstractClassifier
import weka.classifiers.Evaluation
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instances
import java.io.File
import java.util.*
import java.util.stream.IntStream

/**
 * Created by warrior on 10/20/16.
 */

const val ROOT = "./HomeWorks/res/master-homework1"
const val ESTIMATION_FOLDER = "$ROOT/estimation"
const val CLASSIFICATION_RESULTS = "$ROOT/classification_results"
const val NUMBER_OF_ESTIMATED_ATTRIBUTES = 1000
val MAPPER = ObjectMapper()

fun main(args: Array<String>) {
    val train = InstanceCreator.classifiedInstancesFromFile("$ROOT/train")
    val test = InstanceCreator.classifiedInstancesFromFile("$ROOT/valid")

    libsvm.svm.svm_set_print_string_function { it ->  }

    calculateAttributeEstimation(train)
    eval(train, test)
}

private fun calculateAttributeEstimation(data: List<ClassifiedInstance>) {
    File(ESTIMATION_FOLDER).mkdirs()
    for (fr in FeatureRanker.values()) {
        val info = fr.ranker.rank(data)
        MAPPER.writeValue(File("$ESTIMATION_FOLDER/${fr.name.toLowerCase()}.json"), info)
    }
}

private fun eval(train: List<ClassifiedInstance>, test: List<ClassifiedInstance>) {
    val estimationMap: Map<String, List<Ranker.RankInfo>> = FeatureRanker.values()
            .map { it.name.toLowerCase() }
            .associate { name ->
                name to MAPPER.readValue<List<Ranker.RankInfo>>(File("$ESTIMATION_FOLDER/$name.json"),
                        object : TypeReference<List<Ranker.RankInfo>>() {})
            }
    for (c in Classifier.values()) {
        val name = c.name.toLowerCase()
        val classifier = c.classifier

        File("$CLASSIFICATION_RESULTS/$name").mkdirs()

        estimationMap.forEach { rankerName, estimation ->
            val allAttribute = eval(classifier, train, test)

            val sortedIndices = estimation.map { it.index }
            val bestAttributes = IntStream.range(1, NUMBER_OF_ESTIMATED_ATTRIBUTES + 1)
                    .parallel()
                    .mapToDouble { i ->
                val subspace = sortedIndices.subList(0, i).toIntArray()
                val transformedTrain = train.map { SubspaceClassifierInstance(it, subspace) }
                val transformedTest = test.map { SubspaceClassifierInstance(it, subspace) }

                val res = eval(classifier, transformedTrain, transformedTest)
                println("$name-$rankerName-$i: $res")
                res
            }.toArray()

            val reversedSortedIndices = sortedIndices.reversed()
            val worstAttributes = IntStream.range(1, NUMBER_OF_ESTIMATED_ATTRIBUTES + 1)
                    .parallel()
                    .mapToDouble { i ->
                val subspace = reversedSortedIndices.subList(0, i).toIntArray()
                val transformedTrain = train.map { SubspaceClassifierInstance(it, subspace) }
                val transformedTest = test.map { SubspaceClassifierInstance(it, subspace) }

                val res = eval(classifier, transformedTrain, transformedTest)
                println("$name-$rankerName-$i: $res")
                res
            }.toArray()

            MAPPER.writeValue(File("$CLASSIFICATION_RESULTS/$name/$name-$rankerName-best.json"), Result(allAttribute, bestAttributes))
            MAPPER.writeValue(File("$CLASSIFICATION_RESULTS/$name/$name-$rankerName-worst.json"), Result(allAttribute, worstAttributes))
        }
    }
}

private fun eval(classifier: AbstractClassifier, train: List<ClassifiedInstance>, test: List<ClassifiedInstance>): Double {
    val cl = AbstractClassifier.forName(classifier.javaClass.name, classifier.options)
    cl.buildClassifier(train.toInstances())

    val testInstances = test.toInstances()
    val eval = Evaluation(testInstances)
    eval.evaluateModel(cl, testInstances)
    return eval.unweightedMacroFmeasure()
}

fun List<ClassifiedInstance>.toInstances(): Instances {
    if (isEmpty()) {
        throw IllegalArgumentException()
    }
    val instance = get(0)
    val attributeNumber = instance.attributeNumber
    val attributes = ArrayList<Attribute>(attributeNumber + 1)
    for (i in 0 until attributeNumber) {
        val attr = Attribute(instance.getAttributeName(i))
        attributes += attr
    }
    val classAttr = Attribute("class", (0 until instance.classNumber).map(Int::toString).toList())
    attributes += classAttr
    val instances = Instances("datatset", attributes, size)
    instances.setClassIndex(attributes.lastIndex)
    for (inst in this) {
        val values = DoubleArray(attributes.size) { i -> if (i < attributeNumber) inst.getAttributeValue(i) else inst.classId.toDouble()}
        instances += DenseInstance(1.0, values)
    }
    return instances
}

data class Result @JsonCreator constructor(
        @param:JsonProperty("allAttributes") @get:JsonProperty("allAttributes") val allAttributes: Double,
        @param:JsonProperty("bestAttributes") @get:JsonProperty("bestAttributes") val bestAttributes: DoubleArray
)

package com.ifmo.machinelearning.library.classifiers.trees

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier
import com.ifmo.machinelearning.library.core.ClassifiedInstance
import com.ifmo.machinelearning.library.core.SubspaceClassifierInstance
import java.util.*
import java.util.stream.IntStream

/**
 * Created by warrior on 10/19/16.
 */
class RandomForest(
        data: List<ClassifiedInstance>,
        val criterion: QualityCriterion,
        val stopSize: Int,
        val forestSize: Int
) : AbstractInstanceClassifier(data) {

    private val featureSubspaceSize: Int = Math.sqrt(data.size.toDouble()).toInt()
    private val random: Random = Random()

    private lateinit var forest: List<DecisionTree>
    private lateinit var featureSubspaces: List<IntArray>
    private lateinit var indices: List<Set<Int>>

    override fun trainInternal() {
        val (trees, subspaceList, indicesList) = IntStream.range(0, forestSize)
                .parallel()
                .mapToObj { buildForestTree(getData()) }
                .collect(
                        { Triple(ArrayList<DecisionTree>(), ArrayList<IntArray>(), ArrayList<Set<Int>>()) },
                        { acc, t ->
                            acc.first += t.tree
                            acc.second += t.featureSubspace
                            acc.third += t.indices
                        },
                        { acc1, acc2 ->
                            acc1.first += acc2.first
                            acc1.second += acc2.second
                            acc1.third += acc2.third
                        }
                )
        forest = trees
        featureSubspaces = subspaceList
        indices = indicesList
    }

    override fun getSupposedClassId(instance: ClassifiedInstance): Int {
        val results = IntArray(classNumber)
        for ((i, tree) in forest.withIndex()) {
            val featureSubspace = featureSubspaces[i]
            val transformedInstance = SubspaceClassifierInstance(instance, featureSubspace)
            results[tree.getSupposedClassId(transformedInstance)]++
        }
        return results.withIndex().maxBy { it.value }!!.index
    }

    private fun buildForestTree(data: List<ClassifiedInstance>): TreeInfo {
        val sample = ArrayList<ClassifiedInstance>(size)
        val indices = HashSet<Int>(size)
        val featureSubspace = randomFeatureSubspace()

        for (i in 1..size) {
            val index = random.nextInt(size)
            sample += SubspaceClassifierInstance(data[index], featureSubspace)
            indices += index
        }
        val tree = DecisionTree(sample, criterion, stopSize)
        tree.enablePruning(false)
        tree.trainInternal()
        return TreeInfo(tree, featureSubspace, indices)
    }

    private fun randomFeatureSubspace(): IntArray {
        val indices = (0 until size).toMutableList()
        Collections.shuffle(indices, random)
        return IntArray(featureSubspaceSize) { i -> indices[i] }
    }

    fun estimateAttributes(): DoubleArray {
        val errorRates = DoubleArray(size) { index -> calculateOOB(data[index], index) }
        return IntStream.range(0, attributeNumber)
                .parallel()
                .mapToDouble { attributeIndex ->
                    val values = data.map { it.getAttributeValue(attributeIndex) }.toMutableList()
                    Collections.shuffle(values)
                    val transformedInstances = data.zip(values).map { p ->
                        val (instance, value) = p
                        AttributeTransformClassifierInstance(instance, attributeIndex, value)
                    }
                    val errorDifferences = DoubleArray(size) { calculateOOB(transformedInstances[it], it) - errorRates[it] }
                    if (isSameValues(errorDifferences)) {
                        0.0
                    } else {
                        val mean = errorDifferences.average()
                        val stdDeviations = Math.sqrt(variance(errorDifferences, mean))
                        mean / stdDeviations
                    }
                }.toArray()
    }

    private fun calculateOOB(instance: ClassifiedInstance, index: Int): Double {
        var errors = 0.0
        var all = 0.0
        for ((i, tree) in forest.withIndex()) {
            if (index !in indices[i]) {
                all++
                val featureSubspace = featureSubspaces[i]
                val transformedInstance = SubspaceClassifierInstance(instance, featureSubspace)
                val classId = tree.getSupposedClassId(transformedInstance)
                if (classId != instance.classId) {
                    errors++
                }
            }
        }
        return errors / all
    }

    private fun isSameValues(values: DoubleArray): Boolean = values.all { it == values[0] }

    private fun variance(values: DoubleArray, mean: Double): Double {
        if (values.size == 1) {
            return 0.0
        }
        return values.map { v -> (v - mean) * (v - mean) }.sum() / (values.size - 1)
    }

    private data class TreeInfo(val tree: DecisionTree, val featureSubspace: IntArray, val indices: Set<Int>)

    private class AttributeTransformClassifierInstance(
            val instance: ClassifiedInstance,
            val index: Int,
            val value: Double
    ) : ClassifiedInstance by instance {
        override fun getAttributeValue(i: Int): Double = if (i == index) value else instance.getAttributeValue(i)
        override fun getValues(): DoubleArray = DoubleArray(attributeNumber) { i -> getAttributeValue(i) }
    }
}

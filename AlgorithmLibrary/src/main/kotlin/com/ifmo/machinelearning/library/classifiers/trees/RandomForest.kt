package com.ifmo.machinelearning.library.classifiers.trees

import com.ifmo.machinelearning.library.classifiers.AbstractInstanceClassifier
import com.ifmo.machinelearning.library.core.ClassifiedInstance
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
        forest.forEachIndexed { i, tree ->
            val featureSubspace = featureSubspaces[i]
            val transformedInstance = FeatureSubspaceClassifierInstance(instance, featureSubspace)
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
            sample += FeatureSubspaceClassifierInstance(data[index], featureSubspace)
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

    private data class TreeInfo(val tree: DecisionTree, val featureSubspace: IntArray, val indices: Set<Int>)

    private class FeatureSubspaceClassifierInstance(val instance: ClassifiedInstance, val featureSubspace: IntArray) : ClassifiedInstance by instance {
        override fun getAttributeNumber(): Int = featureSubspace.size
        override fun getAttributeName(i: Int): String = instance.getAttributeName(featureSubspace[i])
        override fun getAttributeValue(i: Int): Double = instance.getAttributeValue(featureSubspace[i])
        override fun getValues(): DoubleArray = DoubleArray(featureSubspace.size) { i -> getAttributeValue(i) }
        override fun toString(): String = "${values.joinToString(", ", "[", "]")} -> $classId"
    }
}
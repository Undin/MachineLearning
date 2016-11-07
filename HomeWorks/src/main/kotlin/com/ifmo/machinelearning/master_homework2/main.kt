package com.ifmo.machinelearning.master_homework2

import com.ifmo.machinelearning.library.core.InstanceCreator
import com.ifmo.machinelearning.library.features.transform.PCA
import com.ifmo.machinelearning.visualization.Plot2DBuilder
import java.util.*


/**
 * Created by warrior on 11/7/16.
 */
const val DATA_PATH = "./HomeWorks/res/master-homework2"

fun main(args: Array<String>) {
    for (i in 1..3) {
        showEigenValuesGraph("dataset$i", "$DATA_PATH/dataset$i")
    }
}

private fun showEigenValuesGraph(name: String, datasetPath: String) {
    val instances = InstanceCreator.instancesFromFile(datasetPath)
    val pca = PCA(instances)
    pca.transform()
    val eigenValues = pca.eigenValues

    val trace = eigenValues.sum()
    val sortedIndices = eigenValues.indices.sortedWith(Comparator { i1, i2 -> eigenValues[i2].compareTo(eigenValues[i1]) })
    val normalizedEigenValues = ArrayList<Double>()
    val stickPartLengths = ArrayList<Double>()

    var lengthSum = (1..eigenValues.size).map { 1.0 / it }.sum()
    var index = 0
    while (eigenValues[sortedIndices[index]] / trace > lengthSum / eigenValues.size) {
        normalizedEigenValues += eigenValues[sortedIndices[index]] / trace
        stickPartLengths += lengthSum / eigenValues.size
        index++
        lengthSum -= 1.0 / index
    }

    normalizedEigenValues += eigenValues[sortedIndices[index]] / trace
    stickPartLengths += lengthSum / eigenValues.size

    val builder = Plot2DBuilder("components", "value")
    builder.addBar("λ_i/trC", normalizedEigenValues.toDoubleArray())
    builder.addBar("l_i", stickPartLengths.toDoubleArray())
    builder.show(name, 1200, 600)
}

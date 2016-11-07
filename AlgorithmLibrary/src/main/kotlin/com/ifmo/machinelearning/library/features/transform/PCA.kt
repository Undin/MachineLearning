package com.ifmo.machinelearning.library.features.transform

import com.ifmo.machinelearning.library.core.Instance
import com.ifmo.machinelearning.library.core.InstanceDefaultImpl
import org.jblas.DoubleMatrix
import org.jblas.Eigen
import org.jblas.Geometry
import java.util.*

/**
 * Created by warrior on 11/7/16.
 */
class PCA(data: List<Instance>) : FeatureTransformer(data) {

    lateinit var mainComponents: IntArray
        private set
    lateinit var eigenValues: DoubleArray
        private set

    override fun transform(): List<Instance> {
        val dataMatrix = data.toMatrix()
        Geometry.centerColumns(dataMatrix)
        val covarianceMatrix = covarianceMatrix(dataMatrix)
        val (eigenVectors, eigenValuesMatrix) = Eigen.symmetricEigenvectors(covarianceMatrix)

        val eigenValues = DoubleArray(eigenValuesMatrix.rows) { i ->
            val v = eigenValuesMatrix[i, i]
            if (v < 0) 0.0 else v
        }
        this.eigenValues = eigenValues
        val components = mainComponents(eigenValues)
        this.mainComponents = components
        val mainVectors = eigenVectors.get(IntArray(eigenVectors.rows) { it }, components)
        val transformedData = dataMatrix.mmul(mainVectors)
        return transformedData.toInstances()
    }

    private fun covarianceMatrix(centeredMatrix: DoubleMatrix): DoubleMatrix {
        val attributes = centeredMatrix.columns
        val instances = centeredMatrix.rows
        val covarianceMatrix = DoubleMatrix(attributes, attributes)
        for (i in 0 until attributes) {
            for (j in i until attributes) {
                var cov = 0.0
                for (k in 0 until instances) {
                    cov += centeredMatrix[k, i] * centeredMatrix[k, j]
                }
                cov /= instances - 1
                covarianceMatrix.put(i, j, cov)
                covarianceMatrix.put(j, i, cov)
            }
        }
        return covarianceMatrix
    }

    private fun mainComponents(eigenValues: DoubleArray): IntArray {
        val trace = eigenValues.sum()
        val sortedIndices = eigenValues.indices.sortedWith(Comparator { i1, i2 -> eigenValues[i2].compareTo(eigenValues[i1]) })
        val indices = ArrayList<Int>()

        var sum = (1..eigenValues.size).map { 1.0 / it }.sum()
        var index = 0
        while (eigenValues[sortedIndices[index]] / trace > sum / eigenValues.size) {
            indices += sortedIndices[index]
            index++
            sum -= 1.0 / index
        }
        return indices.toIntArray()
    }

    private fun List<Instance>.toMatrix(): DoubleMatrix {
        val attributeNumber = get(0).attributeNumber
        val matrix = DoubleMatrix(size, attributeNumber)
        for ((i, instance) in this.withIndex()) {
            for (j in 0 until attributeNumber) {
                matrix.put(i, j, instance.getAttributeValue(j))
            }
        }
        return matrix
    }

    private fun DoubleMatrix.toInstances(): List<Instance> {
        val attributes = (0 until columns).map { "attr$it" }.toTypedArray()
        val instances = ArrayList<Instance>(rows)
        for (i in 0 until rows) {
            instances += InstanceDefaultImpl(attributes, DoubleArray(columns) { j -> get(i, j) })
        }
        return instances
    }
}

package com.ifmo.machinelearning.library.core

/**
 * Created by warrior on 10/23/16.
 */
open class SubspaceClassifierInstance(
        val instance: ClassifiedInstance,
        val featureSubspace: IntArray
) : ClassifiedInstance by instance {
    override fun getAttributeNumber(): Int = featureSubspace.size
    override fun getAttributeName(i: Int): String = instance.getAttributeName(featureSubspace[i])
    override fun getAttributeValue(i: Int): Double = instance.getAttributeValue(featureSubspace[i])
    override fun getValues(): DoubleArray = DoubleArray(featureSubspace.size) { i -> getAttributeValue(i) }
    override fun toString(): String = "${values.joinToString(", ", "[", "]")} -> $classId"
}

package com.ifmo.machinelearning.master_homework1

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.ifmo.machinelearning.library.classifiers.trees.GiniGain
import com.ifmo.machinelearning.library.classifiers.trees.RandomForest
import com.ifmo.machinelearning.library.core.ClassifiedInstance
import infodynamics.measures.mixed.kraskov.MutualInfoCalculatorMultiVariateWithDiscreteKraskov
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation

/**
 * Created by warrior on 10/21/16.
 */
interface RankFunction {
    fun rank(data: List<ClassifiedInstance>, attributeIndex: Int): Double
}

class PearsonsCorrelationRankFunction : RankFunction {
    override fun rank(data: List<ClassifiedInstance>, attributeIndex: Int): Double {
        val attributeValues = DoubleArray(data.size) { i -> data[i].getAttributeValue(attributeIndex) }
        val classValues = DoubleArray(data.size) { i -> data[i].classId.toDouble() }
        if (isSameValues(attributeValues) || isSameValues(classValues)) {
            return 0.0
        }
        val correlation = PearsonsCorrelation()
        return correlation.correlation(attributeValues, classValues)
    }
}

class SpearmansCorrelationRankFunction : RankFunction {
    override fun rank(data: List<ClassifiedInstance>, attributeIndex: Int): Double {
        val attributeValues = DoubleArray(data.size) { i -> data[i].getAttributeValue(attributeIndex) }
        val classValues = DoubleArray(data.size) { i -> data[i].classId.toDouble() }
        if (isSameValues(attributeValues) || isSameValues(classValues)) {
            return 0.0
        }
        val correlation = SpearmansCorrelation()
        return correlation.correlation(attributeValues, classValues)
    }
}

class MutualInformationRankFunction : RankFunction {
    override fun rank(data: List<ClassifiedInstance>, attributeIndex: Int): Double {
        val calculator = MutualInfoCalculatorMultiVariateWithDiscreteKraskov()
        calculator.initialise(1, data[0].classNumber)
        val attributeValues = Array(data.size) { i -> DoubleArray(1) { data[i].getAttributeValue(attributeIndex) } }
        val classValues = IntArray(data.size) { i -> data[i].classId }

        calculator.setObservations(attributeValues, classValues)
        return calculator.computeAverageLocalOfObservations()
    }
}

interface Ranker {
    fun rank(data: List<ClassifiedInstance>): List<RankInfo>

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RankInfo @JsonCreator constructor(
            @param:JsonProperty("index") @get:JsonProperty("index") val index: Int,
            @param:JsonProperty("value") @get:JsonProperty("value") val value: Double
    )
}

open class StatisticRanker(val rankFunction: RankFunction) : Ranker {

    override fun rank(data: List<ClassifiedInstance>): List<Ranker.RankInfo> {
         return (0 until data[0].attributeNumber)
                .map { Ranker.RankInfo(it, rankFunction.rank(data, it)) }
                .sortedByDescending { it.value }
    }
}

class PearsonsCorrelationRanker : StatisticRanker(PearsonsCorrelationRankFunction())
class SpearmansCorrelationRanker : StatisticRanker(SpearmansCorrelationRankFunction())
class MutualInformationRanker : StatisticRanker(MutualInformationRankFunction())
class RandomForestRanker : Ranker {
    override fun rank(data: List<ClassifiedInstance>): List<Ranker.RankInfo> {
        val attrNumber = data[0].attributeNumber
        val estimations = (1..10).map {
            val rf = RandomForest(data, GiniGain(), 3, 200)
            rf.train()
            rf.estimateAttributes()
        }
        return (0 until attrNumber).map { i -> estimations.map { it[i] }.average() }
                .mapIndexed { i, value -> Ranker.RankInfo(i, value) }
                .sortedByDescending { it.value }
    }
}

private fun isSameValues(values: DoubleArray): Boolean = values.all { it == values[0] }

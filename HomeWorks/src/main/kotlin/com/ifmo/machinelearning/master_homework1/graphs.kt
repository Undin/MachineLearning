package com.ifmo.machinelearning.master_homework1

import com.fasterxml.jackson.databind.ObjectMapper
import com.ifmo.machinelearning.visualization.Plot2DBuilder
import java.io.File

/**
 * Created by warrior on 10/24/16.
 */

const val MAX_ATTRIBUTE_NUMBER = 100

fun main(args: Array<String>) {
    val mapper = ObjectMapper()
    val quality = listOf("best", "worst")

    for (classifier in Classifier.values().map { it.name.toLowerCase() }) {
        val builder = Plot2DBuilder("attributes", "F-measure")
        for (ranker in FeatureRanker.values().map { it.name.toLowerCase() }) {
            for (q in quality) {
                val fileName = "$CLASSIFICATION_RESULTS/$classifier/$classifier-$ranker-$q.json"
                val results = mapper.readValue(File(fileName), Result::class.java).bestAttributes
                val bests = results.take(MAX_ATTRIBUTE_NUMBER).toDoubleArray()
                builder.addPlot("$ranker-$q", DoubleArray(bests.size, Int::toDouble), bests)
            }
        }
        builder.show(classifier, 1200, 600)
    }
}

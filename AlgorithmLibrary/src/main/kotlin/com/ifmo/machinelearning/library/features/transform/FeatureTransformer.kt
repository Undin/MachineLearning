package com.ifmo.machinelearning.library.features.transform

import com.ifmo.machinelearning.library.core.Instance

/**
 * Created by warrior on 11/7/16.
 */
abstract class FeatureTransformer(protected val data: List<Instance>) {
    abstract fun transform(): List<Instance>
}
